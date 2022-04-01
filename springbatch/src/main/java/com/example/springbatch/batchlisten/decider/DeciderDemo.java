package com.example.springbatch.batchlisten.decider;

import com.example.springbatch.batchlisten.JobCompletionListener;
import com.example.springbatch.entity.UserEntity;
import com.example.springbatch.jiaoyan.CsvBeanValidator;
import com.example.springbatch.jiaoyan.CsvItemProcessor;
import com.example.springbatch.write.MyCustomerLineAggregator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.validator.Validator;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DeadlockLoserDataAccessException;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
@EnableBatchProcessing
public class DeciderDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;
    HashMap map = new HashMap<String,String>();
    @Bean
    public Job DeciderDemoJob(){
        return jobBuilderFactory.get("DeciderDemoJob")
                .start(deciderDemoStep1())
                .next(myDecider())
                .from(myDecider()).on("even").to(deciderDemoStep2())
                .from(myDecider()).on("odd").to(deciderDemoStep3())
                .from(deciderDemoStep3()).on("*").to(myDecider())
                .end().build();
    }
    @Bean
    public Step deciderDemoStep1()  {
        return stepBuilderFactory.get("deciderDemoStep1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("deciderDemoStep1");
                        tets();
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }


    @Bean
    public Step deciderDemoStep2(){
        return stepBuilderFactory.get("deciderDemoStep2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("deciderDemoStep2");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }
    @Bean
    public Step deciderDemoStep3() {
       return stepBuilderFactory.get("deciderDemoStep3")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("deciderDemoStep3");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
       /* return stepBuilderFactory.get("deciderDemoStep3") .<UserEntity, UserEntity>chunk(20)
                // 给step绑定 reader
                .reader(reader())
                // 给step绑定 processor
                .processor(processor())
                // 给step绑定 writer
                .writer(writer(dataSource))
                //开启容错
                .faultTolerant()
                // 设定一个我们允许的这个step可以跳过的异常数量，假如我们设定为3，则当这个step运行时，只要出现的异常数目不超过3，整个step都不会fail。注意，若不设定skipLimit，则其默认值是0
                .skipLimit(3)
                // 指定我们可以跳过的异常，因为有些异常的出现，我们是可以忽略的
                .skip(Exception.class)
                //重试次数
                .retryLimit(3)
                //重试条件
                .retry(ConnectException.class)
                .retry(DeadlockLoserDataAccessException.class)
                // 出现这个异常我们不想跳过，因此这种异常出现一次时，计数器就会加一，直到达到上限
                .noSkip(FileNotFoundException.class)
                .build();*/

    }




    // 用来读取数据
    /*  FileCleanTasklet 清除压缩文件
     *  DefaultLineMapper->lineTokenizer ->delimitedLineTokenizer对文件里面的数据进行|分割，映射成一个类
     * FileWriter 将读取的内容写到数据库中
     * FileProcessor  对读取的内容做过滤处理
     * FlatFileItemReader 读取文本文件
     * DecompressTasklet 对压缩文件进行解压
     * */
    @Bean
    public ItemReader<UserEntity> reader() {
        int count = 0;
        // FlatFileItemReader是一个用来加载文件的itemReader
        FlatFileItemReader<UserEntity> reader = new FlatFileItemReader<>();
        //JdbcPagingItemReaderBuilder<UserEntity> userEntityJdbcPagingItemReaderBuilder = new JdbcPagingItemReaderBuilder<UserEntity>().pageSize();
        // 跳过第一行的标题
        reader.setLinesToSkip(1);
        // 设置csv的位置
        reader.setResource(new ClassPathResource("data1.txt"));
        reader.setEncoding("UTF-8");
        // 设置每一行的数据信息
        reader.setLineMapper(new DefaultLineMapper<UserEntity>(){{
            setLineTokenizer(new DelimitedLineTokenizer(){{
                // 配置了四行文件
                setNames(new String[]{"userName","sex","age", "address"});
                // 配置列于列之间的间隔符,会通过间隔符对每一行进行切分
                setDelimiter("\t");
            }});

            // 设置要映射的实体类属性
            setFieldSetMapper(new BeanWrapperFieldSetMapper<UserEntity>(){{
                setTargetType(UserEntity.class);
            }});
        }});
        return reader;
    }


    // 用来处理数据
    @Bean
    public ItemProcessor<UserEntity,UserEntity> processor(){

        // 使用我们自定义的ItemProcessor的实现CsvItemProcessor
        CsvItemProcessor processor = new CsvItemProcessor();
        // 为processor指定校验器为CsvBeanValidator()
        processor.setValidator(csvBeanValidator());
        return processor;
    }


    // 用来输出数据 可以

    @Bean
    public ItemWriter<UserEntity> writer(@Qualifier("dataSource") DataSource dataSource)  {
        // 通过Jdbc写入到数据库中
        JdbcBatchItemWriter writer = new JdbcBatchItemWriter();
        writer.setDataSource(dataSource);
        // setItemSqlParameterSourceProvider 表示将实体类中的属性和占位符一一映射
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        // 设置要执行批处理的SQL语句。其中占位符的写法是 `:属性名`
        writer.setSql("insert into sys_user(user_name, sex, age, address, status, create_time) " +
                "values(:userName, :sex, :age, :address, :status, :createTime)");
        return writer;
    }



    @Bean
    public Validator<UserEntity> csvBeanValidator(){
        return new CsvBeanValidator<>();
    }

    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionListener();
    }


    @Bean
    public JobExecutionDecider myDecider(){
        return new MyDecider();
    }

    public static Map<String,String> tets(){
        HashMap<String, String> map = new HashMap<>();
        if(1==1){
            map.put("test","2");
            System.out.println("进入二");
        }else{
            map.put("test","3");
            System.out.println("进入三");
        }
    return map;
    }
}


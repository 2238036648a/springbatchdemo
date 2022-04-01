package com.example.springbatch.conf;

import com.example.springbatch.batchlisten.JobCompletionListener;
import com.example.springbatch.batchlisten.decider.MyDecider;
import com.example.springbatch.entity.UserEntity;
import com.example.springbatch.entity.UserEntityCopy;
import com.example.springbatch.jiaoyan.CsvBeanValidator;
import com.example.springbatch.jiaoyan.CsvItemProcessor;
import com.example.springbatch.write.MyCustomerLineAggregator;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.validator.Validator;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.http.HttpRequest;

import javax.annotation.Resource;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//@Configuration
/*@EnableBatchProcessing注解可以为JobRepository提供自动配置。*/
//@EnableBatchProcessing
public class CsvBatchJobConfig {

    @Autowired
    private DataSource dataSource;


    /**
     * 配置一个要执行的Job任务， 包含一个或多个Step
     */
    @Bean
    public Job csvJob(JobBuilderFactory jobBuilderFactory, @Qualifier("csvStep") Step step/*,@Qualifier("step2") Step step2*/) {
        // 为 job 起名为 csvJob
        return jobBuilderFactory.get("csvJob1")
                .start(step)
 //               .next(step2)
                .listener(listener())
                .build();
    }

    // 配置一个Step
    @Bean
    public Step csvStep(
            StepBuilderFactory stepBuilderFactory,
            ItemReader<UserEntity> reader1,
            ItemProcessor<UserEntity,UserEntity> processor1,
            ItemWriter<UserEntity> writer) {

        return stepBuilderFactory.get("csvStep")
                // 批处理每次提交5条数据
                .<UserEntity, UserEntity>chunk(20)
                // 给step绑定 reader
                .reader(reader1)
                // 给step绑定 processor
                .processor(processor1)
                // 给step绑定 writer
                .writer(writer)
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
                .build();
    }


  /*  @Bean
    public Step step2(
            StepBuilderFactory stepBuilderFactory,
            ItemReader<UserEntity> reader1,
            ItemProcessor<UserEntity,UserEntity> processor,
            ItemWriter<UserEntity> writer) {

        return stepBuilderFactory.get("step2")
                // 批处理每次提交5条数据
                .<UserEntity, UserEntity>chunk(20)
                // 给step绑定 reader
                .reader(reader1)
                // 给step绑定 processor
                .processor(processor)
                // 给step绑定 writer
                .writer(writer)
                .build();
    }*/


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

   /* @Bean
    public ItemReader<UserEntity> reader1() {
        int count = 0;
        // FlatFileItemReader是一个用来加载文件的itemReader
        FlatFileItemReader<UserEntity> reader = new FlatFileItemReader<>();
        //JdbcPagingItemReaderBuilder<UserEntity> userEntityJdbcPagingItemReaderBuilder = new JdbcPagingItemReaderBuilder<UserEntity>().pageSize();
        // 跳过第一行的标题
        reader.setLinesToSkip(1);
        // 设置csv的位置
        reader.setResource(new ClassPathResource("data.csv"));
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
*/
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

   /* @Bean  //可以
    public FlatFileItemWriter<UserEntity> writer() throws Exception {
        FlatFileItemWriter<UserEntity> itemWriter = new FlatFileItemWriter<>();
        String path ;
        File file = new File("E:\\java代码\\springBatch\\out.xml");
        path = file.getAbsolutePath();
        System.out.println(">> file is created in: " + path);
        itemWriter.setResource(new FileSystemResource(path));
        //MyCustomerLineAggregator，即自定义输出的数据格式的转换器，其实可以理解为，
        // 你从数据库读取出来的数据，最后输出到文件中展现的格式是怎样的呢？可以有json类型的，也可以是上面的csv类型的，
        // 甚至可以是xml的类型，
        itemWriter.setLineAggregator(new MyCustomerLineAggregator());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }
*/



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

}


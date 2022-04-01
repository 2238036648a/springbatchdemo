package com.example.springbatch.readdb.batch.conf;

import com.example.springbatch.batchlisten.JobCompletionListener;
import com.example.springbatch.jiaoyan.CsvBeanValidator;
import com.example.springbatch.jiaoyan.CsvItemProcessor;
import com.example.springbatch.readdb.entity.UserEntity;
import com.example.springbatch.readdb.batch.listener.JdbcItemJobListener;
import com.example.springbatch.readdb.batch.process.JdbcItemProcessor;
import com.example.springbatch.readdb.batch.write.JdbcItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description : 处理具体工作业务  主要包含三个部分:读数据、处理数据、写数据
 */
//@Configuration
//@EnableBatchProcessing // 开启批处理  系统会在启动时执行，阻止自动执行job需要在配置文件添加配置
public class BatchConfiguration {

    /**
     * 作业仓库
     */
    /*@Bean  使用数据库记录日志
    public JobRepository jobRepository(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception{
        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setDataSource(dataSource);
        jobRepositoryFactoryBean.setTransactionManager(transactionManager);
        jobRepositoryFactoryBean.setDatabaseType(DatabaseType.MYSQL.name());

        return jobRepositoryFactoryBean.getObject();
    }*/


    /**
     * 作业调度器
     */
    /*public SimpleJobLauncher jobLauncher(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception{
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(this.jobRepository(dataSource, transactionManager));
        return jobLauncher;
    }*/
    //使用默认提供方式
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    // tag::readerwriterprocessor[] 1.读数据
    //比较常用的从数据库读数据的有JdbcCursorItemReader,JdbcPagingItemReader等。
  /*  @Bean
    public ItemReader<UserEntity> reader(DataSource dataSource) throws UnexpectedInputException, ParseException, Exception {
        //读数据可以读文件、数据库等，我这只使用了读取数据库的方式

        JdbcCursorItemReader<UserEntity> itemReader = new JdbcCursorItemReader<UserEntity>();

        itemReader.setDataSource(dataSource);
        itemReader.setSql("select * from  sys_user");
        itemReader.setRowMapper(new BeanPropertyRowMapper<UserEntity>(UserEntity.class));*//*要转换成的bean*//*
        ExecutionContext executionContext = new ExecutionContext();
        itemReader.open(executionContext);
        Object customerCredit = new Object();
        while(customerCredit != null){
            customerCredit = itemReader.read();
        }
        itemReader.close();
        return itemReader;
    }
*/
    //可以
    @Bean
    public JdbcPagingItemReader<UserEntity> reader(DataSource dataSource) {
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("status", "1");
        JdbcPagingItemReader<UserEntity> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource); // 设置数据源
        reader.setFetchSize(1); // 设置一次最大读取条数
        reader.setRowMapper(new PersonRowMapper()); // 把数据库中的每条数据映射到user对中
        reader.setParameterValues(parameterValues);


        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
       // OraclePagingQueryProvider queryProvider = new OraclePagingQueryProvider();
        queryProvider.setSelectClause("id,user_name,sex,age,address,status,create_time,update_time"); // 设置查询的列
        queryProvider.setFromClause("from sys_user"); // 设置要查询的表
        queryProvider.setWhereClause("where status=:status");

        Map<String, Order> sortKeys = new HashMap<String, Order>();// 定义一个集合用于存放排序列
        sortKeys.put("id", Order.ASCENDING);// 按照升序排序
        queryProvider.setSortKeys(sortKeys);
        reader.setQueryProvider(queryProvider);// 设置排序列

        return reader;
    };
/*
    @Bean       不行
    @StepScope
    public StaxEventItemReader<UserEntity> reader() {
        StaxEventItemReader<UserEntity> reader=new StaxEventItemReader<UserEntity>();
        reader.setResource(new ClassPathResource("data.xml"));
        reader.setFragmentRootElementName("data");
        XStreamMarshaller unmarshaller=new XStreamMarshaller();
        Map<String,Class> map=new HashMap<>();
        map.put("data", UserEntity.class);

        unmarshaller.setAliases(map);
        reader.setUnmarshaller(unmarshaller);//序列化
        return reader;

    }
*/

    //2.处理数据
    @Bean
    public JdbcItemProcessor processor() {

        return new JdbcItemProcessor();
    }

    //3.写数据
    @Bean
    public JdbcItemWriter writer(JdbcTemplate jdbcTemplate) {
        //自定义写数据操作
        JdbcItemWriter writer = new JdbcItemWriter(jdbcTemplate);
        return writer;
    }

    // end::readerwriterprocessor[]

    // tag::jobstep[] 生成job
    // (1)状态机：例完成step1，是否继续完成step2,step3,我们就需要通过Job flow来控制
    //（2）进行演示:使用next()方法来达到顺序执行step1，step2…的目的，再使用on(),to(),
    // from()方法达到与next()方法同样的目的，再展示fail()方法和stopAndRestart()方法；
    @Bean
    public Job importJob(JobBuilderFactory jobs, @Qualifier("step1")Step s1,@Qualifier("step2")Step s2, JdbcItemJobListener listener) {
        return jobs.get("importJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(s1)
                .end()
                .build();
        //当step1 成功执行完成后，返回COMPLETED， 才调用step2进行下一步处理。但是过多的step，不易于程序维护和复用
                /*.start(step1())
                .on("COMPLETED").to(step2())
                .from(step2()).on("COMPLETED").to(step3())
                .from(step3()).end()
                .build();*/
    }

    //执行步骤
    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<UserEntity> reader,
                      JdbcItemWriter jdbcItemWriter, ItemProcessor<UserEntity, UserEntity> processor) {
        return stepBuilderFactory.get("step1")
                .<UserEntity, UserEntity> chunk(3)   //一次处理多少数据
                .reader(reader)
                .processor(processor)
                .writer(jdbcItemWriter)
                .build();
    }
}

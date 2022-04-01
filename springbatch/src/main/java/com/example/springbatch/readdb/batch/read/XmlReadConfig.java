package com.example.springbatch.readdb.batch.read;

import com.example.springbatch.readdb.batch.listener.JdbcItemJobListener;
import com.example.springbatch.readdb.batch.process.JdbcItemProcessor;
import com.example.springbatch.readdb.batch.write.JdbcItemWriter;
import com.example.springbatch.readdb.entity.UserEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;

//@Configuration
//@EnableBatchProcessing
public class XmlReadConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Resource
    @Qualifier("xmlFileWrite")
    private ItemWriter<? super UserEntity> xmlFileWrite;
    @Bean
    public Job importJob(JobBuilderFactory jobs, @Qualifier("step1") Step s1, JdbcItemJobListener listener) {
        return jobs.get("importJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(s1)
                .end()
                .build();
    }

    //执行步骤
    @Bean
    public Step step1(ItemWriter<UserEntity> erite) {
        return stepBuilderFactory.get("xmldemo")
                .<UserEntity, UserEntity> chunk(2)   //一次处理多少数据
                .reader(xmlFileReader())
                .writer(erite)
                .processor(process())
                .build();
    }

    @Bean
    public ItemWriter<UserEntity> erite(ItemWriter<UserEntity> userEntityItemWriter) {

        if(1==1){
            System.out.println("----------"+userEntityItemWriter);

        }

        return userEntityItemWriter;
    }


    private StaxEventItemReader<UserEntity> xmlFileReader() {
        StaxEventItemReader<UserEntity> reader = new StaxEventItemReader<>();
        reader.setResource(new ClassPathResource("data.xml"));
        //要处理的跟标签
        reader.setFragmentRootElementName("data");
        //将对象转化为xml  spring提供的XStreamMarshaller
        XStreamMarshaller marshaller = new XStreamMarshaller();
        reader.setEncoding("UTF-8");
        HashMap<String, Object> map = new HashMap<>();
        map.put("data",UserEntity.class);
        marshaller.setAliases(map);
        reader.setUnmarshaller(marshaller);
        return reader;
    }

    @Bean
    public ItemProcessor<UserEntity ,UserEntity> process(){
        JdbcItemProcessor jdbcItemProcessor = new JdbcItemProcessor();
        /*UserEntity userEntity = new UserEntity();

        if (userEntity.getSex().equals("1")) {
            userEntity.setSex("男");
        } else {
            userEntity.setSex("2");
        }

        // 设置默认值

        return userEntity;*/
        return jdbcItemProcessor;

    }

}

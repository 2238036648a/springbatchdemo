package com.example.springbatch.controller;

import com.example.springbatch.entity.UserEntity;
import com.example.springbatch.service.UserService;
import com.example.springbatch.service.impl.UserServiceImpl;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
public class JobController {

    @Autowired
    private UserService userService;
    @Resource
    private JobLauncher jobLauncher;
    @Resource
    private Job job;

    @GetMapping("/doJob")
  // @Scheduled(cron ="0 0/1 * * * ?")
    public void doJob() {
            try {
            // 同一个job执行多次, 由于job定义一样, 则无法区分jobInstance, 所以增加jobParameter用于区分
            JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
            jobParametersBuilder.addDate("jobDate", new Date());
               // ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/data1.txt");
               // Job job = (Job)context.getBean("completionPolicyJob");
            // 执行一个批处理任务
            jobLauncher.run(job,jobParametersBuilder.toJobParameters());
            } catch (Exception e) {
                e.printStackTrace();
        }

    }


    @GetMapping("/doJob1")
    // @Scheduled(cron ="0 0/1 * * * ?")
    public void doJob1() {
        List<UserEntity> userEntities = userService.queryInfoById();
    }


}


package com.example.springbatch.readdb.controller;

import com.example.springbatch.service.UserService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
public class UserController {

    @Resource
    private JobLauncher jobLauncher;
    @Resource
    private Job job;

    @GetMapping("/batchdb")
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
}

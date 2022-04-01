package com.example.springbatch.readdb.batch.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class JdbcItemJobListener extends JobExecutionListenerSupport {


    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            System.out.println("!!! JOB 执行完成!");
        }
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        // TODO Auto-generated method stub
        super.beforeJob(jobExecution);
        System.out.println("!!! JOB 执行开始!");
    }
}

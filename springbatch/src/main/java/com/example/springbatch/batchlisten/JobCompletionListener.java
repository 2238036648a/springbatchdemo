package com.example.springbatch.batchlisten;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

public class JobCompletionListener extends JobExecutionListenerSupport {
    // 用于批处理开始前执行
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println(String.format("任务id=%s开始于%s", jobExecution.getJobId(), jobExecution.getStartTime()));
    }

    // 用于批处理开始后执行
    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            System.out.println(String.format("任务id=%s结束于%s", jobExecution.getJobId(), jobExecution.getEndTime()));
        } else {
            System.out.println(String.format("任务id=%s执行异常状态=%s", jobExecution.getJobId(),  jobExecution.getStatus()));
        }
    }
}


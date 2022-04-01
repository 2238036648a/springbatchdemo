package com.example.springbatch.batchlisten.lunch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;

public interface JobLauncher {
    public JobExecution run(Job job, JobParameters jobParameters);
}


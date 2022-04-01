package com.example.springbatch.batchlisten.lunch;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.batch.runtime.StepExecution;
import java.util.Date;
import java.util.Set;

//@Controller
public class StopListener {
    @Autowired
    private JobOperator jobOperator;

    //@Scheduled(cron ="0 0/1 * * * ?")
    public void stopConditionsMet() throws NoSuchJobException, NoSuchJobExecutionException, JobExecutionNotRunningException {

        Set<String> jobNames = jobOperator.getJobNames();
        for (String bb:jobNames
             ) {
            System.out.println("111-------"+bb);
        }

        // Set<Long> executions = jobOperator.getRunningExecutions("csvJob1");
   //     jobOperator.stop(executions.iterator().next());
       //   jobOperator.stop(sunningExecutions.get(0));

        //要获取正在运行的作业的列表，请执行以下操作：
       /* Set<String> jobNames = jobOperator.getJobNames();

        for (String aa: jobNames  ) {
            Set<JobExecution> jobExecutions = jobExplorer.findRunningJobExecutions(aa);
            for (JobExecution bb: jobExecutions   ) {
                jobOperator.stop(bb.getId());
            }
        }
*/
    }

    // @Scheduled(cron ="0 0/2 * * * ?")
    /*public void stopjob2() throws NoSuchJobException, NoSuchJobExecutionException, JobExecutionNotRunningException {
        Set<JobExecution> executions = jobExplorer.findRunningJobExecutions("csvJob1");
        for(JobExecution execution : executions){
            if (execution.getStatus() == BatchStatus.STARTED) {
                jobOperator.stop(execution.getId());
            }
        }
    }*/

}

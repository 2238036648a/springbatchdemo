package com.example.springbatch.batchlisten.decider;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyDecider implements JobExecutionDecider {
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        BatchStatus status = jobExecution.getStatus();
        System.out.println("----------------"+status);


        ExitStatus exitStatus = jobExecution.getExitStatus();
        System.out.println("---------"+exitStatus);
        /*int count = 0;
        //每次执行count+1
        count++;
        if (count % 2 == 0) {
            //count为偶数时返回even
            return new FlowExecutionStatus("even");
        }
        //奇数时返回odd
        return new FlowExecutionStatus("odd");*/
       /* Map<String, String> tets = DeciderDemo.tets();
        Set<Map.Entry<String, String>> entries = tets.entrySet();
        for (int i = 0; i <entries.size() ; i++) {
            Map.Entry<String, String> next = entries.iterator().next();
            String value = next.getValue();
            if(value.equals("3")){
                return new FlowExecutionStatus("even");

            }else{
                return new FlowExecutionStatus("odd");
            }
        }
            return new FlowExecutionStatus("bb");*/

    return null;
    }
}

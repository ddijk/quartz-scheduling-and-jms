package com.airhacks;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobImpl implements Job {

    private static int n;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Running job " + jobExecutionContext.getJobDetail().getDescription() + " - " +n++);
    }
}

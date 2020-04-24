package com.airhacks;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import javax.enterprise.inject.Produces;

public class SchedulerProvider {

    @Produces
    public Scheduler provide() throws SchedulerException {

        return StdSchedulerFactory.getDefaultScheduler();
    }
}

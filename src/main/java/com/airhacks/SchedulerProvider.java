package com.airhacks;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import javax.enterprise.inject.Produces;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class SchedulerProvider {

    @Produces
    public Scheduler provide() throws SchedulerException, IOException {

        Properties properties = new Properties();
        properties.load(new InputStreamReader(SchedulerProvider.class.getResourceAsStream("/quartz.properties")));
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory(properties);
        return stdSchedulerFactory.getScheduler();
    }
}

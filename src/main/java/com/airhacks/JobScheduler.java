package com.airhacks;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.text.ParseException;
import java.util.List;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Singleton
@Startup
public class JobScheduler {


    private static final Logger LOGGER = LoggerFactory.getLogger(JobScheduler.class);

    @Inject
    private Job[] jobs;

    private Scheduler scheduler;

    @PostConstruct
    public void init() {

        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            for (Job job : jobs) {

                Trigger trigger = createCronTrigger(job);
                JobDetail jobDetail = createJobDetail(job);
                scheduler.scheduleJob(jobDetail, trigger);
                LOGGER.info("Job {} scheduled", job.getName());
            }
            LOGGER.info("Scheduler initialized");
        } catch (SchedulerException | ParseException ex) {
            LOGGER.error("Failed to start Scheduler ", ex);

            throw new IllegalStateException("Failed to start Scheduler", ex);
        }
    }

    private JobDetail createJobDetail(Job job) {
        return newJob(JobImpl.class).withIdentity(job.getName() ).withDescription(job.getName()).build();
    }

    private Trigger createCronTrigger(Job job) throws ParseException {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronScheduleNonvalidatedExpression(job.getSchedule());

        return newTrigger().withIdentity(job.getName()).withSchedule(cronScheduleBuilder).build();
    }

    @PreDestroy
    public void shutdown() {
        LOGGER.info("Scheduler is shutting down.");
        try {
            scheduler.shutdown(true);
        } catch (SchedulerException e) {
            LOGGER.info("Scheduler is shut down FAILED.", e);
        }
    }
}

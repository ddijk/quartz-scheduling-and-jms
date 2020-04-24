package com.airhacks;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Topic;
import java.text.ParseException;
import java.util.List;

import static com.airhacks.JobImpl.EVENT_NAME;
import static com.airhacks.JobImpl.JMS_CONTEXT;
import static com.airhacks.JobImpl.JMS_TOPIC;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Singleton
@Startup
public class JobScheduler {


    private static final Logger LOGGER = LoggerFactory.getLogger(JobScheduler.class);

    @Inject
    private Job[] jobs;

    @Inject
    private Scheduler scheduler;

    @Inject
    @ApplicationScoped
    JMSContext jmsContext;

    @Resource(lookup = "jms/myTopic2")
    @ApplicationScoped
    Topic topic;

    @PostConstruct
    public void init() {

        try {
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

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(JMS_CONTEXT, jmsContext);
        jobDataMap.put(JMS_TOPIC, topic);
        jobDataMap.put(EVENT_NAME, job.getEventName());
        jobDataMap.put("callback", this);

        return newJob(JobImpl.class).usingJobData(jobDataMap).withIdentity(job.getName() ).withDescription(job.getName()).build();
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
            throw new RuntimeException("Scheduler is shut down FAILED");
        }
    }

    public void send() {

        LOGGER.info("Received callback.");
        final JMSProducer producer = jmsContext.createProducer();

        Message msg = jmsContext.createTextMessage("See how this flies");
        try {
            msg.setStringProperty("NewsType" , "Sports");
            producer.send(topic, msg);
            LOGGER.info("Msg sent.");
        } catch (JMSException e) {
            LOGGER.info("Msg NOT sent. "+e);
        }


    }
}

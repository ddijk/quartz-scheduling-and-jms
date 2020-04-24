package com.airhacks;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Topic;

public class JobImpl implements Job {
    public static final String JMS_CONTEXT = "jmsContext";
    public static final String JMS_TOPIC = "topic";
    public static final String EVENT_NAME = "eventName";
    private static int n;
    private static final Logger LOGGER = LoggerFactory.getLogger(JobImpl.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        final JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        final String eventName = jobDataMap.getString(EVENT_NAME);

        final String description = jobExecutionContext.getJobDetail().getDescription();
       LOGGER.error("Running job '{}' met eventName '{}'", description, eventName);

        JMSContext jmsContext = (JMSContext) jobDataMap.get(JMS_CONTEXT);
        Topic topic = (Topic) jobDataMap.get(JMS_TOPIC);

        final JMSProducer producer = jmsContext.createProducer();

        Message event  = jmsContext.createMessage();
        try {
            event.setStringProperty("naam", eventName);
            event.setStringProperty("bron", "workerscheduler");
            producer.send(topic, event);
           LOGGER.error("Event verzonden door '{}' met eventName '{}'", description, eventName);
        } catch (JMSException e) {
           LOGGER.error("Fout bij het maken van message {}", e);
        }

       LOGGER.error("Running job " + jobExecutionContext.getJobDetail().getDescription() + " - " +n++);
    }
}

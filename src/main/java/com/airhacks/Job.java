package com.airhacks;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.enterprise.inject.Produces;
import java.io.IOException;

public class Job {

    String name;
    String schedule;
    String eventName;

    public String getName() {
        return name;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Produces
    public Job[] jobs() throws IOException {
        ObjectMapper om = new ObjectMapper();

        return om.readValue(Job.class.getResource("/jobs.json"), Job[].class);

    }
}

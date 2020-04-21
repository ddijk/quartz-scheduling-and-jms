package com.airhacks;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.enterprise.inject.Produces;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Job {

    String name;
    String schedule;
    String jobClass;

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

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    @Produces
    public Job[] jobs() throws IOException {
        ObjectMapper om = new ObjectMapper();

        return om.readValue(Job.class.getResource("/jobs.json"), Job[].class);

    }
}

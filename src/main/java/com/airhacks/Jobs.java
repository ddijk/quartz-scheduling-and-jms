package com.airhacks;

import java.util.List;

public class Jobs {

    List<Job> jobs;

    public static class Job {

        String name;
        String schedule;

        public String getName() {
            return name;
        }

        public String getSchedule() {
            return schedule;
        }
    }
}

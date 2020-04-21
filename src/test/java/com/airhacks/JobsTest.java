package com.airhacks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class JobsTest {

    @Test
    public void verifyDeserialization() throws IOException {


        ObjectMapper om = new ObjectMapper();


     Job[] jobs =   om.readValue(Job.class.getResource("/test-jobs.json"), Job[].class);

        assertEquals(3, jobs.length);
//        assertEquals();
    }
}
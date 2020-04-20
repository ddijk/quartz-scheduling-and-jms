package com.airhacks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class JobsTest {

    @Test
    public void verifyDeserialization() throws IOException {


        ObjectMapper om = new ObjectMapper();


     Jobs.Job[] jobs =   om.readValue(Jobs.class.getResource("/test-jobs.json"), Jobs.Job[].class);

        assertEquals(3, jobs.length);
//        assertEquals();
    }
}
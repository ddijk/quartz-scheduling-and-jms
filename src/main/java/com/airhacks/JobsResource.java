package com.airhacks;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.IOException;

@Path("job")
public class JobsResource {

    @GET
    public Jobs.Job[] get() throws IOException {
        ObjectMapper om = new ObjectMapper();


        Jobs.Job[] jobs =   om.readValue(Jobs.class.getResource("/jobs.json"), Jobs.Job[].class);

        return jobs;
    }
}

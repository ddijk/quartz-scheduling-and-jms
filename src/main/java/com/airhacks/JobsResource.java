package com.airhacks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.util.List;

@Path("job")
public class JobsResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobScheduler.class);

    @Inject
    List<Job> jobs;

    @GET
    @Produces("application/json")
    public List<Job> get() throws IOException {

        System.out.println("Dit is stdout");
        LOGGER.error("Dit is slf4j level error");
        LOGGER.info("Dit is slf4j level info");
        return jobs;
    }
}

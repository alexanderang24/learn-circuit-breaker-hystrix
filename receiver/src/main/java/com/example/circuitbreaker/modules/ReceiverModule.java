package com.example.circuitbreaker.modules;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class ReceiverModule {

    @PostMapping("/receiver")
    @HystrixCommand(fallbackMethod = "fallback",
            commandProperties = {
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "3000"
                    )
            }
    )
    public String receiver() {
        log.info("Received request from sender");
        if(RandomUtils.nextBoolean()) {
            throw new RuntimeException("Failed!");
        }
        log.info("Success");
        return "Success!";
    }

    public String fallback() {
        log.info("Circuit broken");
        return "Circuit broken";
    }
}

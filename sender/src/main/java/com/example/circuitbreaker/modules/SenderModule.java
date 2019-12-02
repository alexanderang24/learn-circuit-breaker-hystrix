package com.example.circuitbreaker.modules;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


@RestController
@Slf4j
public class SenderModule {

    @Autowired SenderService senderService;

    @PostMapping("/sender")
    @HystrixCommand(fallbackMethod = "fallback",
            commandProperties = {
                    @HystrixProperty(
                            name = "circuitBreaker.enabled",
                            value = "true"
                    ),
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "3000"
                    ),
                    @HystrixProperty(
                            name = "fallback.isolation.semaphore.maxConcurrentRequests",
                            value = "1000"
                    ),
                    @HystrixProperty(
                            name = "circuitBreaker.requestVolumeThreshold",
                            value = "1000"
                    ),
                    @HystrixProperty(
                            name = "circuitBreaker.errorThresholdPercentage",
                            value = "-1"
                    ),
                    @HystrixProperty(
                            name = "metrics.rollingStats.timeInMilliseconds",
                            value = "10000"
                    )
            }
    )
    public String sender() {
        log.info("Sender");
        try {
            return senderService.getResult().get();
        } catch (Exception ex) {
            throw new NullPointerException();
        }
    }

    public String fallback() {
        log.info("Circuit broken");
        return "Circuit broken";
    }

}
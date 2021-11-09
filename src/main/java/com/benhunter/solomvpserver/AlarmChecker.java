package com.benhunter.solomvpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Component
public class AlarmChecker {

    private int count = 0;

    private static final Logger log = LoggerFactory.getLogger(AlarmChecker.class);

    private AlarmRepository repository;

    AlarmChecker(AlarmRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    public void sendLog() {
        log.info("Running @Scheduled method. Count: " + this.count);
        this.count += 1;
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    public void checkAlarms() {

        for (Alarm alarm : this.repository.findAll()) {
            log.info("Checking alarm id: " + alarm.getId()
                    + " name: " + alarm.getName()
            );
            checkSingleAlarm(alarm);
        }
    }



    private final RestTemplate restTemplate = new RestTemplate();

    public void checkSingleAlarm(Alarm alarm) {
        String message = "{\"content\":\"Hello discord\"}";

        // TODO send the messge to Discord only if alarm.target is down.
        RequestEntity<String> request = RequestEntity.post(alarm.getWebhook())
                .contentType(MediaType.APPLICATION_JSON)
                .body(message);
        restTemplate.exchange(request, Void.class);
    }
}

package com.benhunter.solomvpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
        boolean targetIsDown = false;  // Flag for the status of alarm.target. Determines whether the webhook executes.


        // Send a request to alarm.target and check the response code.
        try {
            RequestEntity<Void> targetRequest = RequestEntity.get(alarm.getTarget()).build();
            ResponseEntity<String> targetResponse = restTemplate.exchange(targetRequest, String.class);
            if (targetResponse.getStatusCode().isError()) {
                log.info("Alarm name: " + alarm.getName() + " responded with an error!");
                targetIsDown = true;
            }
        } catch (Exception e) {
            log.info("Exception: " + e.getMessage());
            targetIsDown = true;
        }

        // Send the message to Discord only if alarm.target is down, but was up last time it was checked.
        if (targetIsDown && alarm.getTargetStatusUp()) {
            final String messageTargetDown = "{\"content\":\"" + alarm.getName() + " is down! \"}";

            // Send the webhook to notify the user.
            log.info("Sending webhook for alarm name: " + alarm.getName());
            RequestEntity<String> webhookRequest = RequestEntity.post(alarm.getWebhook())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(messageTargetDown);
            restTemplate.exchange(webhookRequest, Void.class);  // Not doing anything with the response.

            // Update the alarm.
            alarm.setTargetStatusUp(false);
            this.repository.save(alarm);
        } else {
            if (targetIsDown) {
                log.info("Target was already down. Not sending webhook for alarm name: " + alarm.getName());
            } else {
                log.info("Target is up. Not sending webhook for alarm name: " + alarm.getName());
            }
        }

        // Target is up, but was down last time it was checked.
        if (!targetIsDown && !alarm.getTargetStatusUp()) {
            final String messageTargetDown = "{\"content\":\"" + alarm.getName() + " is up! \"}";

            log.info("Target is up! Sending webhook.");
            RequestEntity<String> webhookRequest = RequestEntity.post(alarm.getWebhook())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(messageTargetDown);
            restTemplate.exchange(webhookRequest, Void.class);  // Not doing anything with the response.

            // Update the alarm.
            alarm.setTargetStatusUp(true);
            this.repository.save(alarm);
        }
    }
}

package com.benhunter.solomvpserver;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final Logger log;

    private final RestTemplate restTemplate = new RestTemplate();

    private final AlarmRepository repository;

    @Autowired
    AlarmChecker(AlarmRepository repository, final Logger log) {
        this.repository = repository;
        this.log = log;
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    public void sendLog() {
        log.info("Running @Scheduled method. Count: " + this.count);
        this.incrementCount();
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    public void checkAlarms() {

        for (Alarm alarm : this.repository.findAll()) {
            if (alarm.getEnabled()) {
                log.info("Checking alarm id: " + alarm.getId()
                        + " name: " + alarm.getName()
                );
                checkSingleAlarm(alarm);
            }
        }
    }

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
            log.info("Exception while checking target of alarm id: " + alarm.getId() + " Exception: " + e.getMessage());
            targetIsDown = true;
        }

        // Target is down, but was up last time it was checked.
        if (targetIsDown && alarm.getTargetStatusUp()) {
            final String messageTargetDown = "{\"content\":\"" + alarm.getName() + " is down! \"}";

            // Send the webhook to notify the user.
            log.info("Sending webhook for alarm name: " + alarm.getName());
            RequestEntity<String> webhookRequest = RequestEntity.post(alarm.getWebhook())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(messageTargetDown);
            try {
                restTemplate.exchange(webhookRequest, Void.class);  // Not doing anything with the response.
            } catch (IllegalArgumentException exception) {
                log.info("Bad webhook URL provided for alarm id: "
                        + alarm.getId().toString()
                        + " Exception: "
                        + exception.getMessage());
            }

            // Update the alarm.
            alarm.setTargetStatusUp(false);
            this.repository.save(alarm);
            return;
        } else {
            if (targetIsDown) {
                log.debug("Target was already down. Not sending webhook for alarm name: " + alarm.getName());
            } else {
                log.debug("Target is up. Not sending webhook for alarm name: " + alarm.getName());
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

    public int getCount() {
        return count;
    }

    private void incrementCount() {
        this.count += 1;
    }

}

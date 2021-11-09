package com.benhunter.solomvpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
        }
    }

//    @Async
//    public Future<String> asyncChecker(Alarm alarm) {
//        // TODO Check the target of alarm
//    }
}

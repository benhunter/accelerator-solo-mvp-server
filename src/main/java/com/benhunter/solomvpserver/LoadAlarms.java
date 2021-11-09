package com.benhunter.solomvpserver;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class LoadAlarms {

    private static final Logger log = LoggerFactory.getLogger(LoadAlarms.class);

    @Bean
    CommandLineRunner initAlarms(AlarmRepository repository) {
        Alarm one = new Alarm()
                .setName("web.benhunter.me")
                .setTarget("http://web.benhunter.me")
                .setWebhook("webhook");
        return args -> {
            log.info("Preloading " + repository.save(one));
        };
    }
}

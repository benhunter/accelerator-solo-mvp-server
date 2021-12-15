package com.benhunter.solomvpserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@SpringBootTest
class AlarmCheckerTest {


    @InjectMocks
    private AlarmChecker alarmChecker;

    @MockBean
    AlarmRepository alarmRepository;

    @Mock
//    @MockBean
    Logger logger;

    @BeforeEach
    void setUp() {
        this.alarmChecker = new AlarmChecker(this.alarmRepository, this.logger);
    }

    @Test
    void sendLog() {
        var initialCount = alarmChecker.getCount();
        this.alarmChecker.sendLog();
        assertEquals(initialCount + 1, alarmChecker.getCount());
        verify(logger).info(anyString());
    }

    @Test
    void checkAlarms() {
    }

    @Test
    void checkSingleAlarm() {
    }
}
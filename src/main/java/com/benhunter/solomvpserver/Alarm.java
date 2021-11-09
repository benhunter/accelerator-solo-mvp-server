package com.benhunter.solomvpserver;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@Accessors(chain = true)
public class Alarm {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String name;  // User-friendly name to identify an alarm.
    private String target;  // The endpoint to monitor.
    private String action = "HTTP";  // The type of service to monitor.
    private Integer intervalSeconds = 1;  // Interval in seconds between checks.
    private String webhook;  // The discord webhook where alarms will be sent.
    private Boolean targetStatusUp = true;  // True when the target is up.
    private Boolean didNotify = false;  // Whether the webhook was sent to notify change of status. TODO: remove?
}

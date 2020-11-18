package com.monitor.web.alarm.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "ams_subscriber")
public class SubscriberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long alarmId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String subscriber;

    @Column(nullable = false)
    private int isActive;

    @Column(nullable = false)
    private int category;
}

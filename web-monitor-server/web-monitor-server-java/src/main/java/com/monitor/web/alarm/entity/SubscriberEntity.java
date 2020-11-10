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
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @NotNull(message = "alarmId不能为空")
    private Long alarmId;

    @NotEmpty(message = "subscriber不能为空")
    private String subscriber;

    @NotNull(message = "isActive不能为空")
    private int isActive;

    @NotNull(message = "category不能为空")
    private int category;
}

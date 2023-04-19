package com.events.eventPlanner.domain;

import lombok.Data;

import java.sql.Date;

@Data
public class Event {

    private Integer id;
    private String name;
    private Date date;
    private String description;
    private Place place;
}

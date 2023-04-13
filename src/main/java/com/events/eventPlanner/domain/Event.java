package com.events.eventPlanner.domain;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Data
@Component
public class Event {
    private int id;
    private String name;
    private Date date;
    private String description;

    @Autowired
    private Place place;
}

package com.events.eventPlanner.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    private Integer id;
    private String name;
    private Date date;
    private String description;
    private Place place;
}

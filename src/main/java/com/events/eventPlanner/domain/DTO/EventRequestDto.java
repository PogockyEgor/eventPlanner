package com.events.eventPlanner.domain.DTO;

import lombok.Data;

import java.sql.Date;

@Data
public class EventRequestDto {

    private Integer id;
    private String name;
    private Date date;
    private String description;
    private Integer placeID;
}

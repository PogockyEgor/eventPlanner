package com.events.eventPlanner.domain.DTO;

import com.events.eventPlanner.domain.Place;
import lombok.Data;

import java.sql.Date;

@Data
public class EventResponseDto {

    private Integer id;
    private String name;
    private Date date;
    private String description;
    private Integer countOfVisitors;
    private Place place;
}

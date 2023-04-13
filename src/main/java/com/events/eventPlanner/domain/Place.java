package com.events.eventPlanner.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Place {
    private int id;
    private String address;
    private String district;
    private String description;
    private float rating;
}

package com.events.eventPlanner.domain;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Place {

    private Integer id;
    private String name;
    private String address;
    private String district;
    private String description;
    private float rating;
    private Set<Event> events = new HashSet<>();
}

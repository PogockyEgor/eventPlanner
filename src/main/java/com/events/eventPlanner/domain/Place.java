package com.events.eventPlanner.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Place {

    private Integer id;
    private String name;
    private String address;
    private String district;
    private String description;
    private float rating;
    private Set<Event> events = new HashSet<>();
}

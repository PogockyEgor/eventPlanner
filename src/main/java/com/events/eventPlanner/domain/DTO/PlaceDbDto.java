package com.events.eventPlanner.domain.DTO;

import com.events.eventPlanner.domain.Event;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "place")
@EqualsAndHashCode(exclude = "events")
@ToString(exclude = "events")

public class PlaceDbDto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "place_id_seq_gen")
    @SequenceGenerator(name = "place_id_seq_gen", sequenceName = "place_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "address")
    private String address;

    @Column(name = "district")
    private String district;

    @Column(name = "description")
    private String description;

    @Column(name = "rating")
    private float rating;

    @OneToMany(mappedBy = "place", fetch = FetchType.EAGER)
    private Set<EventDbDto> events = new HashSet<>();
}

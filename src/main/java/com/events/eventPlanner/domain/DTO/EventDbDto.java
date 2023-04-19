package com.events.eventPlanner.domain.DTO;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.Date;

@Data
@Entity
@Table(name = "events")
@EqualsAndHashCode(exclude = "place")
@ToString(exclude = "place")
public class EventDbDto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "place_id_seq_gen")
    @SequenceGenerator(name = "place_id_seq_gen", sequenceName = "place_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "event_name")
    private String name;

    @Column(name = "event_date")
    private Date date;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private PlaceDbDto place;
}
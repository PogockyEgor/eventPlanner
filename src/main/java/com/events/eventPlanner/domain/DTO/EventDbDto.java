package com.events.eventPlanner.domain.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
@EqualsAndHashCode(exclude = "place")
@ToString(exclude = "place")
public class EventDbDto {

    @Id
    @Column(name = "id")
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
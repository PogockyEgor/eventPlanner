package com.events.eventPlanner.domain.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "place")
@EqualsAndHashCode(exclude = "events")
@ToString(exclude = "events")

public class PlaceDbDto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "place_id_seq_gen")
    @SequenceGenerator(name = "place_id_seq_gen", sequenceName = "place_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "name")
    private String name;

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

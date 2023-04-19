package com.events.eventPlanner.repository;

import com.events.eventPlanner.domain.DTO.EventDbDto;
import com.events.eventPlanner.domain.DTO.EventRequestDto;
import com.events.eventPlanner.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventDbDto, Integer> {

    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO events(event_name, event_date, description, place_id)  values (event_name, event_date, description, place_id)")
    void addEvent(Event event);
}
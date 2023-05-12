package com.events.eventPlanner.repository;

import com.events.eventPlanner.domain.DTO.EventDbDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface EventRepository extends JpaRepository<EventDbDto, Integer> {

    @Query(nativeQuery = true, value = "SELECT count(event_id) FROM events INNER JOIN l_users_events lue on events.id = lue.event_id WHERE event_id=:eventId")
    int getCountOfUsersOnEvent(int eventId);

    @Query(nativeQuery = true, value = "SELECT events.id, event_name, event_date, description, place_id FROM events INNER JOIN l_users_events lue on events.id = lue.event_id WHERE lue.user_id=:userId")
    ArrayList<EventDbDto> getAllEventsForUser(int userId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM events WHERE event_date < CURRENT_DATE")
    int deletePastEvents();
}
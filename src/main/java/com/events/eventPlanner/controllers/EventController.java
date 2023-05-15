package com.events.eventPlanner.controllers;

import com.events.eventPlanner.domain.DTO.EventRequestDto;
import com.events.eventPlanner.domain.DTO.EventResponseDto;
import com.events.eventPlanner.domain.DTO.UserResponseDto;
import com.events.eventPlanner.domain.Event;
import com.events.eventPlanner.service.EventService;
import com.events.eventPlanner.service.PlaceService;
import com.events.eventPlanner.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/event")
public class EventController {

    EventService eventService;
    UserService userService;
    PlaceService placeService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public EventController(EventService eventService, UserService userService, PlaceService placeService) {
        this.eventService = eventService;
        this.userService = userService;
        this.placeService = placeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable int id) {
        logger.info("get request to /event/id");
        EventResponseDto eventResponseDto = eventService.getEventById(id);
        return new ResponseEntity<>(eventResponseDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllEvents() {
        logger.info("get request to /event");
        ArrayList<Event> allEvents = eventService.getAllEvents();
        return new ResponseEntity<>(allEvents, HttpStatus.OK);
    }

    @GetMapping("countOfVisitors/{id}")
    public ResponseEntity<?> getCountOfVisitors(@PathVariable int id) {
        logger.info("get request to /event/countOfVisitors");
        long countOfVisitors = eventService.getCountOfVisitors(id);
        return new ResponseEntity<>(countOfVisitors, HttpStatus.OK);
    }

    @GetMapping("/visitors/{id}")
    public ResponseEntity<?> getVisitorsOfEvent(@PathVariable int id) {
        logger.info("get request to /event/visitors");
        ArrayList<UserResponseDto> visitors = eventService.getVisitorsOfEvent(id);
        return new ResponseEntity<>(visitors, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addEvent(@RequestBody @Valid EventRequestDto eventRequestDto) {
        logger.info("post request to /event");
        Event event = eventService.createEvent(eventRequestDto);
        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateEvent(@RequestBody @Valid EventRequestDto eventRequestDto) {
        logger.info("put request to /event");
        Event event = eventService.updateEvent(eventRequestDto);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable int id) {
        logger.info("delete request to /event/id");
        eventService.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/pastEvents")
    public ResponseEntity<?> deletePastEvents() {
        logger.info("delete request to /events/pastEvents");
        eventService.deletePastEvents();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
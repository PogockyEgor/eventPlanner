package com.events.eventPlanner.controllers;

import com.events.eventPlanner.domain.DTO.EventRequestDto;
import com.events.eventPlanner.domain.DTO.EventResponseDto;
import com.events.eventPlanner.domain.DTO.UserResponseDto;
import com.events.eventPlanner.domain.Event;
import com.events.eventPlanner.domain.User;
import com.events.eventPlanner.exceptions.AppError;
import com.events.eventPlanner.service.EventService;
import com.events.eventPlanner.service.PlaceService;
import com.events.eventPlanner.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Objects;

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
        if (eventResponseDto == null) {
            logger.warn("Event with id = " + id + " not found");
            return new ResponseEntity<>(new AppError("event with id = " + id + " not found",
                    HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(eventResponseDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllEvents() {
        logger.info("get request to /event");
        ArrayList<Event> allEvents = eventService.getAllEvents();
        if (allEvents.isEmpty()) {
            return new ResponseEntity<>(new AppError("Don't found any events",
                    HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(allEvents, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addEvent(@RequestBody @Valid EventRequestDto eventRequestDto) {
        logger.info("post request to /event");
        User user = userService.findUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!Objects.equals(user.getRole(), "admin")) {
            if (user.getId() != placeService.getAdminOfPlace(eventRequestDto.getPlaceID())) {
                return new ResponseEntity<>(new AppError("You are not admin of this place",
                        HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
            }
        }
        if (eventService.createEvent(eventRequestDto) == null) {
            return new ResponseEntity<>(new AppError("Event was not created",
                    HttpStatus.NO_CONTENT.value()), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateEvent(@RequestBody @Valid EventRequestDto eventRequestDto) {
        logger.info("put request to /event");
        User user = userService.findUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!Objects.equals(user.getRole(), "admin")) {
            if (user.getId() != placeService.getAdminOfPlace(eventRequestDto.getPlaceID())) {
                return new ResponseEntity<>(new AppError("You are not admin of this place",
                        HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
            }
        }
        if (eventService.getEventById(eventRequestDto.getId()) == null) {
            return new ResponseEntity<>(
                    new AppError("Event with id = " + eventRequestDto.getId() + " not found", HttpStatus.NOT_FOUND.value()),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(eventService.updateEvent(eventRequestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable int id) {
        logger.info("delete request to /event/id");
        User user = userService.findUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!Objects.equals(user.getRole(), "admin")) {
            if (user.getId() != placeService.getAdminOfPlace(id)) {
                return new ResponseEntity<>(new AppError("You are not admin of this place",
                        HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
            }
        }
        if (eventService.getEventById(id) == null) {
            return new ResponseEntity<>(
                    new AppError("Event with id = " + id + " not found", HttpStatus.NOT_FOUND.value()),
                    HttpStatus.NOT_FOUND);
        }
        eventService.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/pastEvents")
    public ResponseEntity<?> deletePastEvents() {
        logger.info("delete request to /events/pastEvents");
        if (eventService.deletePastEvents() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("countOfVisitors/{id}")
    public ResponseEntity<?> getCountOfVisitors(@PathVariable int id) {
        logger.info("get request to /event/countOfVisitors");
        if (eventService.getEventById(id) == null) {
            return new ResponseEntity<>(
                    new AppError("Event with id = " + id + " not found", HttpStatus.NOT_FOUND.value()),
                    HttpStatus.NOT_FOUND);
        }
        long countOfVisitors = eventService.getCountOfVisitors(id);
        return new ResponseEntity<>(countOfVisitors, HttpStatus.OK);
    }

    @GetMapping("/visitors/{eventId}")
    public ResponseEntity<?> getVisitorsOfEvent(@PathVariable int eventId) {
        logger.info("get request to /event/visitors");
        User user = userService.findUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!Objects.equals(user.getRole(), "admin")) {
            if (user.getId() != placeService.getAdminOfPlace(eventService.getEventById(eventId).getId())) {
                return new ResponseEntity<>(new AppError("You are not admin of this place",
                        HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
            }
        }
        if (eventService.getEventById(eventId) == null) {
            logger.warn("Event with id = " + eventId + " not found");
            return new ResponseEntity<>(new AppError("event with id = " + eventId + " not found",
                    HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
        ArrayList<UserResponseDto> visitors = eventService.getUsersForEvent(eventId);
        if (visitors.isEmpty()) {
            return new ResponseEntity<>(
                    new AppError("No events for this user", HttpStatus.NOT_FOUND.value()),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(visitors, HttpStatus.OK);
    }
}
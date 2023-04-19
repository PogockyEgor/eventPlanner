package com.events.eventPlanner.controllers;

import com.events.eventPlanner.domain.DTO.EventRequestDto;
import com.events.eventPlanner.domain.Event;
import com.events.eventPlanner.domain.Place;
import com.events.eventPlanner.service.EventService;
import com.events.eventPlanner.service.PlaceService;
import com.events.eventPlanner.utils.AppError;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/event")
public class EventController {

    EventService eventService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlace(@PathVariable int id){
        Event event = eventService.getEventById(id);
        if (event == null) {
            logger.warn("Place with id = "+ id +" not found");
            return new ResponseEntity<>(new AppError("event with id = " + id + " not found",
                    HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllEvents() {
        ArrayList<Event> allEvents = eventService.getAllEvents();
        if (allEvents.isEmpty()) {
            return new ResponseEntity<>(new AppError("Don't found any places",
                    HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(allEvents, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addEvent(@RequestBody @Valid EventRequestDto eventRequestDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            for (ObjectError o : bindingResult.getAllErrors()) {
                logger.warn("We have bindingResult error : " + o);
            }
        }
        if (eventService.createEvent(eventRequestDto) == null) {
            return new ResponseEntity<>(new AppError("Place was not created",
                    HttpStatus.NO_CONTENT.value()), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateEvent(@RequestBody @Valid EventRequestDto eventRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (ObjectError o : bindingResult.getAllErrors()) {
                logger.warn("We have bindingResult error : " + o);
            }
        }
        eventService.updateEvent(eventRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable int id) {
        eventService.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

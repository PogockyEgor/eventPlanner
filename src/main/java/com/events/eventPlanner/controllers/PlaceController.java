package com.events.eventPlanner.controllers;

import com.events.eventPlanner.domain.Place;
import com.events.eventPlanner.domain.User;
import com.events.eventPlanner.exceptions.AppError;
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
@RequestMapping("/place")
public class PlaceController {

    PlaceService placeService;
    UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PlaceController(PlaceService placeService, UserService userService) {
        this.placeService = placeService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlace(@PathVariable int id) {
        logger.info("get request to /place/id");
        Place place = placeService.getPlaceById(id);
        return new ResponseEntity<>(place, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllPlaces() {
        logger.info("get request to /place");
        ArrayList<Place> allPlaces = placeService.getAllPlaces();
        return new ResponseEntity<>(allPlaces, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addPlace(@RequestBody @Valid Place place) {
        logger.info("post request to /place");
        return new ResponseEntity<>(placeService.createPlace(place), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updatePlace(@RequestBody @Valid Place place) {
        logger.info("put request to /place");
        return new ResponseEntity<>(placeService.updatePlace(place), HttpStatus.OK);
    }

    @PutMapping("/admin")
    public ResponseEntity<?> appointAdmin(@RequestParam int placeId, @RequestParam int userId) {
        logger.info("put request to /place/admin");
        placeService.appointAdmin(userId, placeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlace(@PathVariable int id) {
        placeService.deletePlace(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
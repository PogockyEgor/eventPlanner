package com.events.eventPlanner.controllers;

import com.events.eventPlanner.domain.Place;
import com.events.eventPlanner.service.PlaceService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/place")
public class PlaceController {

    PlaceService placeService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
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
        logger.info("delete request to /place");
        placeService.deletePlace(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
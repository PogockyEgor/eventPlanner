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
        if (place == null) {
            logger.warn("Place with id = " + id + " not found");
            return new ResponseEntity<>(new AppError("place with id = " + id + " not found",
                    HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(place, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllPlaces() {
        logger.info("get request to /place");
        ArrayList<Place> allPlaces = placeService.getAllPlaces();
        if (allPlaces.isEmpty()) {
            return new ResponseEntity<>(new AppError("Don't found any places",
                    HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(allPlaces, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addPlace(@RequestBody @Valid Place place) {
        logger.info("post request to /place");
        if (placeService.createPlace(place) == null) {
            return new ResponseEntity<>(new AppError("Place was not created",
                    HttpStatus.NO_CONTENT.value()), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updatePlace(@RequestBody @Valid Place place) {
        logger.info("put request to /place");
        User user = userService.findUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!Objects.equals(user.getRole(), "admin")) {
            if (user.getId() != placeService.getAdminOfPlace(place.getId())) {
                return new ResponseEntity<>(new AppError("You are not admin of this place",
                        HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
            }
        }
        if (placeService.getPlaceById(place.getId()) == null) {
            return new ResponseEntity<>(
                    new AppError("Place with id = " + place.getId() + " not found", HttpStatus.NOT_FOUND.value()),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(placeService.updatePlace(place), HttpStatus.OK);
    }

    @PutMapping("/admin")
    public ResponseEntity<?> appointAdmin(@RequestParam int placeId, @RequestParam int userId) {
        logger.info("put request to /place/admin");
        if (placeService.getPlaceById(placeId) == null) {
            return new ResponseEntity<>(
                    new AppError("Place with id = " + placeId + " not found", HttpStatus.NOT_FOUND.value()),
                    HttpStatus.NOT_FOUND);
        }
        if (userService.getUserById(userId) == null) {
            return new ResponseEntity<>(
                    new AppError("User with id = " + userId + "not found", HttpStatus.NOT_FOUND.value()),
                    HttpStatus.NOT_FOUND);
        }
        placeService.appointAdmin(userId, placeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlace(@PathVariable int id) {
        if (placeService.getPlaceById(id) == null) {
            return new ResponseEntity<>(
                    new AppError("Place with id = " + id + " not found", HttpStatus.NOT_FOUND.value()),
                    HttpStatus.NOT_FOUND);
        }
        placeService.deletePlace(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
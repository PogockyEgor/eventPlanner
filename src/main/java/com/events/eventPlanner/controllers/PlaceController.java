package com.events.eventPlanner.controllers;

import com.events.eventPlanner.domain.Place;
import com.events.eventPlanner.service.PlaceService;
import com.events.eventPlanner.exceptions.AppError;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getPlace(@PathVariable int id){
        Place place = placeService.getPlaceById(id);
        if (place == null) {
            logger.warn("Place with id = "+ id +" not found");
            return new ResponseEntity<>(new AppError("place with id = " + id + " not found",
                    HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(place, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllPlaces() {
        ArrayList<Place> allPlaces = placeService.getAllPlaces();
        if (allPlaces.isEmpty()) {
            return new ResponseEntity<>(new AppError("Don't found any places",
                    HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(allPlaces, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addPlace(@RequestBody @Valid Place place, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            for (ObjectError o : bindingResult.getAllErrors()) {
                logger.warn("We have bindingResult error : " + o);
            }
        }
        if (placeService.createPlace(place) == null) {
            return new ResponseEntity<>(new AppError("Place was not created",
                    HttpStatus.NO_CONTENT.value()), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updatePlace(@RequestBody @Valid Place place, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (ObjectError o : bindingResult.getAllErrors()) {
                logger.warn("We have bindingResult error : " + o);
            }
        }
        placeService.updatePlace(place);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlace(@PathVariable int id) {
        placeService.deletePlace(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

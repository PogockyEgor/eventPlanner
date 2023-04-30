package com.events.eventPlanner.controllers;

import com.events.eventPlanner.domain.DTO.UserResponseDto;
import com.events.eventPlanner.domain.Event;
import com.events.eventPlanner.domain.User;
import com.events.eventPlanner.service.UserService;
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
@RequestMapping("/user")
public class UserController {
    UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        UserResponseDto userResponseDto = userService.getUserById(id);
        if (userResponseDto == null) {
            return new ResponseEntity<>(
                    new AppError("User with id = " + id + " not found", HttpStatus.NOT_FOUND.value()),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        ArrayList<UserResponseDto> allUsers = userService.getAllUsers();
        if (allUsers.isEmpty()) {
            return new ResponseEntity<>(new AppError("Don't found any users",
                    HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (userService.createUser(user) == null) {
            return new ResponseEntity<>(new AppError("User was not created",
                    HttpStatus.NO_CONTENT.value()), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/addEvent")
    public ResponseEntity<?> addEventToUser(@RequestParam int eventID, @RequestParam int userID) {
        if (userService.addEventToUser(eventID, userID) == 0) {
            return new ResponseEntity<>(new AppError("Event was not added",
                    HttpStatus.NO_CONTENT.value()), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (userService.getUserById(user.getId()) == null) {
            return new ResponseEntity<>(
                    new AppError("User with id = " + user.getId() + " not found", HttpStatus.NOT_FOUND.value()),
                    HttpStatus.NOT_FOUND);
        }
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        if (userService.getUserById(id) == null) {
            return new ResponseEntity<>(
                    new AppError("User with id = " + id + " not found", HttpStatus.NOT_FOUND.value()),
                    HttpStatus.NOT_FOUND);
        }
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/myEvents/{userId}")
    public ResponseEntity<?> getAllEventsForUser(@PathVariable int userId){
        if (userService.getUserById(userId) == null) {
            return new ResponseEntity<>(
                    new AppError("User with id = " + userId + " not found", HttpStatus.NOT_FOUND.value()),
                    HttpStatus.NOT_FOUND);
        }
        ArrayList<Event> userEvents = userService.getAllEventsForUser(userId);
        if (userEvents.isEmpty()){
            return new ResponseEntity<>(
                    new AppError("No events for this user", HttpStatus.NOT_FOUND.value()),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userEvents, HttpStatus.OK);
    }

    @DeleteMapping("/deleteEvent")
    public ResponseEntity<?> deleteEventFromUser(@RequestParam int eventID, @RequestParam int userID){
        if (userService.deleteEventFromUser(eventID, userID) == 0) {
            return new ResponseEntity<>(new AppError("Event was not deleted",
                    HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
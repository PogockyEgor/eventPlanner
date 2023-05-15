package com.events.eventPlanner.controllers;

import com.events.eventPlanner.domain.DTO.UserResponseDto;
import com.events.eventPlanner.domain.Event;
import com.events.eventPlanner.domain.User;
import com.events.eventPlanner.exceptions.AppError;
import com.events.eventPlanner.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.Objects;

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
        logger.info("get request to /user/id");
        User user = userService.findUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        if(!Objects.equals(user.getRole(), "admin")){
            if (!user.getId().equals(id)){
                return new ResponseEntity<>(new AppError("You are not this user",
                        HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
            }
        }
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
        logger.info("get request to /user");
        ArrayList<UserResponseDto> allUsers = userService.getAllUsers();
        if (allUsers.isEmpty()) {
            return new ResponseEntity<>(new AppError("Don't found any users",
                    HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid User user) {
        logger.info("post request to /user");
        if (userService.createUser(user) == null) {
            return new ResponseEntity<>(new AppError("User was not created",
                    HttpStatus.NO_CONTENT.value()), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/addEvent")
    public ResponseEntity<?> addEventToUser(@RequestParam int eventId, @RequestParam int userId) {
        logger.info("post request to /user/addEvent");
        User user = userService.findUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        if(!Objects.equals(user.getRole(), "admin")){
            if (!user.getId().equals(userId)){
                return new ResponseEntity<>(new AppError("You are not this user",
                        HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
            }
        }
        if (userService.addEventToUser(eventId, userId) == 0) {
            return new ResponseEntity<>(new AppError("Event was not added",
                    HttpStatus.NO_CONTENT.value()), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody @Valid User user) {
        logger.info("put request to /user");
        User secureUser = userService.findUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        if(!Objects.equals(user.getRole(), "admin")){
            if (!secureUser.getId().equals(user.getId())){
                return new ResponseEntity<>(new AppError("You are not this user",
                        HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
            }
        }
        if (userService.getUserById(user.getId()) == null) {
            return new ResponseEntity<>(
                    new AppError("User with id = " + user.getId() + " not found", HttpStatus.NOT_FOUND.value()),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.updateUser(user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        logger.info("delete request to /user");
        User user = userService.findUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        if(!Objects.equals(user.getRole(), "admin")){
            if (!user.getId().equals(id)){
                return new ResponseEntity<>(new AppError("You are not this user",
                        HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
            }
        }
        if (userService.getUserById(id) == null) {
            return new ResponseEntity<>(
                    new AppError("User with id = " + id + " not found", HttpStatus.NOT_FOUND.value()),
                    HttpStatus.NOT_FOUND);
        }

        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/myEvents/{userId}")
    public ResponseEntity<?> getAllEventsForUser(@PathVariable int userId) {
        logger.info("get request to /user/myEvents/id");
        User user = userService.findUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        if(!Objects.equals(user.getRole(), "admin")){
            if (!user.getId().equals(userId)){
                return new ResponseEntity<>(new AppError("You are not this user",
                        HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
            }
        }
        if (userService.getUserById(userId) == null) {
            return new ResponseEntity<>(
                    new AppError("User with id = " + userId + " not found", HttpStatus.NOT_FOUND.value()),
                    HttpStatus.NOT_FOUND);
        }
        ArrayList<Event> userEvents = userService.getAllEventsForUser(userId);
        if (userEvents.isEmpty()) {
            return new ResponseEntity<>(
                    new AppError("No events for this user", HttpStatus.NOT_FOUND.value()),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userEvents, HttpStatus.OK);
    }

    @DeleteMapping("/deleteEvent")
    public ResponseEntity<?> deleteEventFromUser(@RequestParam int eventId, @RequestParam int userId) {
        logger.info("delete request to /user/deleteEvent");
        User user = userService.findUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        if(!Objects.equals(user.getRole(), "admin")){
            if (!user.getId().equals(userId)){
                return new ResponseEntity<>(new AppError("You are not this user",
                        HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
            }
        }
        if (userService.deleteEventFromUser(eventId, userId) == 0) {
            return new ResponseEntity<>(new AppError("Event was not deleted",
                    HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
package com.events.eventPlanner.service;

import com.events.eventPlanner.domain.DTO.EventDbDto;
import com.events.eventPlanner.domain.DTO.UserResponseDto;
import com.events.eventPlanner.domain.Event;
import com.events.eventPlanner.domain.User;
import com.events.eventPlanner.exceptions.ForbiddenContentException;
import com.events.eventPlanner.exceptions.ObjectNotFoundException;
import com.events.eventPlanner.mappers.DtoMapper;
import com.events.eventPlanner.repository.EventRepository;
import com.events.eventPlanner.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Objects;

@Service
public class UserService {

    UserRepository userRepository;
    EventRepository eventRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, EventRepository eventRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto getUserById(int id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Don't find user with id: " + id));
        checkPermission(id);
        return DtoMapper.fromUserToUserResponseDto(user);
    }

    public ArrayList<UserResponseDto> getAllUsers() {
        ArrayList<User> users = (ArrayList<User>) userRepository.findAll();
        ArrayList<UserResponseDto> usersResponseDto = new ArrayList<>();
        for (User u : users) {
            usersResponseDto.add(DtoMapper.fromUserToUserResponseDto(u));
        }
        if (usersResponseDto.isEmpty()) {
            throw new ObjectNotFoundException("Don't find any users");
        }
        return usersResponseDto;
    }

    public ArrayList<Event> getAllEventsForUser(int id) {
        userRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Don't find user with id: " + id));
        checkPermission(id);
        ArrayList<Event> events = new ArrayList<>();
        for (EventDbDto e : eventRepository.getAllEventsForUser(id)) {
            events.add(DtoMapper.fromEventDbDtoToEvent(e));
        }
        if (events.isEmpty()) {
            throw new ObjectNotFoundException("No events for this user");
        }
        return events;
    }

    public void createUser(User user) {
        user.setCreated(new Date(System.currentTimeMillis()));
        user.setEdited(new Date(System.currentTimeMillis()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("user");
        userRepository.save(user);
    }

    @Transactional
    public void addEventToUser(int eventId, int userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Don't find user with id: " + userId));
        eventRepository.findById(eventId).orElseThrow(
                () -> new ObjectNotFoundException("Don't find event with id: " + eventId));
        checkPermission(userId);
        Date createTime = new Date(System.currentTimeMillis());
        userRepository.addEventToUser(eventId, userId, createTime);
    }

    public void updateUser(User user) {
        userRepository.findById(user.getId()).orElseThrow(
                () -> new ObjectNotFoundException("Don't find user with id: " + user.getId()));
        checkPermission(user.getId());
        user.setEdited(new Date(System.currentTimeMillis()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(int id) {
        userRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Don't find user with id: " + id));
        checkPermission(id);
        userRepository.deleteUser(id);
    }

    @Transactional
    public void deleteEventFromUser(int eventId, int userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Don't find user with id: " + userId));
        eventRepository.findById(eventId).orElseThrow(
                () -> new ObjectNotFoundException("Don't find event with id: " + eventId));
        checkPermission(userId);
        if (userRepository.deleteEventFromUser(eventId, userId)==0){
            throw new ObjectNotFoundException("You are not registered for this event");
        }
    }

    public void checkPermission(int id) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByLogin(login).orElseThrow(
                () -> new ObjectNotFoundException("Don't find user in secure context"));
        if (!Objects.equals(user.getRole(), "admin")) {
            if (!user.getId().equals(id)) {
                throw new ForbiddenContentException("You are not this user");
            }
        }
    }
}
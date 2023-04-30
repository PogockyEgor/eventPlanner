package com.events.eventPlanner.service;

import com.events.eventPlanner.domain.DTO.EventDbDto;
import com.events.eventPlanner.domain.DTO.UserResponseDto;
import com.events.eventPlanner.domain.Event;
import com.events.eventPlanner.domain.User;
import com.events.eventPlanner.mappers.DtoMapper;
import com.events.eventPlanner.repository.EventRepository;
import com.events.eventPlanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.AreaAveragingScaleFilter;
import java.sql.Date;
import java.util.ArrayList;

@Service
public class UserService {

    UserRepository userRepository;
    EventRepository eventRepository;

    @Autowired
    public UserService(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public UserResponseDto getUserById(int id) {
        User user = userRepository.findById(id).orElseThrow();
        return DtoMapper.fromUserToUserResponseDto(user);
    }

    public ArrayList<UserResponseDto> getAllUsers() {
        ArrayList<User> users = (ArrayList<User>) userRepository.findAll();
        ArrayList<UserResponseDto> userResponseDtos = new ArrayList<>();
        for (User u: users){
            userResponseDtos.add(DtoMapper.fromUserToUserResponseDto(u));
        }
        return userResponseDtos;
    }

    public User createUser(User user) {
        user.setCreated(new Date(System.currentTimeMillis()));
        user.setEdited(new Date(System.currentTimeMillis()));
        return userRepository.save(user);
    }

    @Transactional
    public int addEventToUser(int eventID, int userID){
        Date createTime = new Date(System.currentTimeMillis());
        return userRepository.addEventToUser(eventID, userID, createTime);
    }

    public ArrayList<Event> getAllEventsForUser(int userId){
        ArrayList<Event> events = new ArrayList<>();
        for (EventDbDto e: eventRepository.getAllEventsForUser(userId)){
            events.add(DtoMapper.fromEventDbDtoToEvent(e));
        }
        return events;
    }

    @Transactional
    public int deleteEventFromUser(int eventID, int userID){
        return userRepository.deleteEventFromUser(eventID, userID);
    }

    public User updateUser(User user){
        user.setEdited(new Date(System.currentTimeMillis()));
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(int id){
        userRepository.deleteUser(id);
    }
}

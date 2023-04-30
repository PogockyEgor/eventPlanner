package com.events.eventPlanner.service;

import com.events.eventPlanner.domain.DTO.EventDbDto;
import com.events.eventPlanner.domain.DTO.EventRequestDto;
import com.events.eventPlanner.domain.DTO.EventResponseDto;
import com.events.eventPlanner.domain.DTO.UserResponseDto;
import com.events.eventPlanner.domain.Event;
import com.events.eventPlanner.domain.User;
import com.events.eventPlanner.repository.EventRepository;
import com.events.eventPlanner.mappers.DtoMapper;
import com.events.eventPlanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class EventService {
    EventRepository eventRepository;
    UserRepository userRepository;

    @Autowired
    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public EventResponseDto getEventById(int id) {
        Event event = DtoMapper.fromEventDbDtoToEvent(eventRepository.findById(id).orElseThrow());
        EventResponseDto eventResponseDto = DtoMapper.fromEventToEventResponseDto(event);
        eventResponseDto.setCountOfVisitors(eventRepository.getCountOfUsersOnEvent(id));
        return eventResponseDto;
    }

    public ArrayList<Event> getAllEvents() {
        ArrayList<EventDbDto> eventDbDtos = (ArrayList<EventDbDto>) eventRepository.findAll();
        ArrayList<Event> events = new ArrayList<>();
        for (EventDbDto e: eventDbDtos) {
            events.add(DtoMapper.fromEventDbDtoToEvent(e));
        }
        return events;
    }

    public Event createEvent(EventRequestDto eventRequestDto) {
        Event event = DtoMapper.fromEventRequestDtoToEvent(eventRequestDto);
        EventDbDto eventDbDto = DtoMapper.fromEventToEventDbDto(event);
        return DtoMapper.fromEventDbDtoToEvent(eventRepository.save(eventDbDto));
    }

    public Event updateEvent(EventRequestDto eventRequestDto){
        Event event = DtoMapper.fromEventRequestDtoToEvent(eventRequestDto);
        EventDbDto eventDbDto = DtoMapper.fromEventToEventDbDto(event);
        return DtoMapper.fromEventDbDtoToEvent(eventRepository.save(eventDbDto));
    }

    public ArrayList<UserResponseDto> getUsersForEvent(int eventId){
        ArrayList<UserResponseDto> visitors = new ArrayList<>();
        for (User u: userRepository.getAllUsersForEvent(eventId)){
            visitors.add(DtoMapper.fromUserToUserResponseDto(u));
        }
        return visitors;
    }

    @Transactional
    public void deleteEvent(int id){
        eventRepository.deleteById(id);
    }

    @Transactional
    public int getCountOfVisitors(int eventId){
        return eventRepository.getCountOfUsersOnEvent(eventId);
    }
}

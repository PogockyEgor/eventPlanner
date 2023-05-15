package com.events.eventPlanner.service;

import com.events.eventPlanner.domain.DTO.EventDbDto;
import com.events.eventPlanner.domain.DTO.EventRequestDto;
import com.events.eventPlanner.domain.DTO.EventResponseDto;
import com.events.eventPlanner.domain.DTO.UserResponseDto;
import com.events.eventPlanner.domain.Event;
import com.events.eventPlanner.domain.User;
import com.events.eventPlanner.exceptions.ForbiddenContentException;
import com.events.eventPlanner.exceptions.ObjectNotFoundException;
import com.events.eventPlanner.mappers.DtoMapper;
import com.events.eventPlanner.repository.EventRepository;
import com.events.eventPlanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class EventService {
    EventRepository eventRepository;
    UserRepository userRepository;
    UserService userService;
    PlaceService placeService;

    @Autowired
    public EventService(EventRepository eventRepository, UserRepository userRepository, UserService userService,
                        PlaceService placeService) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.placeService = placeService;
    }

    public EventResponseDto getEventById(int id) {
        Event event = DtoMapper.fromEventDbDtoToEvent(eventRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Event with id " + id + " not found")));
        EventResponseDto eventResponseDto = DtoMapper.fromEventToEventResponseDto(event);
        eventResponseDto.setCountOfVisitors(eventRepository.getCountOfUsersOnEvent(id));
        return eventResponseDto;
    }

    public ArrayList<Event> getAllEvents() {
        ArrayList<EventDbDto> eventDbDtos = (ArrayList<EventDbDto>) eventRepository.findAll();
        ArrayList<Event> events = new ArrayList<>();
        for (EventDbDto e : eventDbDtos) {
            events.add(DtoMapper.fromEventDbDtoToEvent(e));
        }
        if (events.isEmpty()) {
            throw new ObjectNotFoundException("don't find any events");
        }
        return events;
    }

    public int getCountOfVisitors(int id) {
        eventRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Event with id " + id + " not found"));
        return eventRepository.getCountOfUsersOnEvent(id);
    }

    public ArrayList<UserResponseDto> getVisitorsOfEvent(int id) {
        EventDbDto eventDbDto = eventRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Event with id " + id + " not found"));
        adminOfPlaceCheck(eventDbDto.getPlace().getId());
        ArrayList<UserResponseDto> visitors = new ArrayList<>();
        for (User u : userRepository.getAllUsersForEvent(id)) {
            visitors.add(DtoMapper.fromUserToUserResponseDto(u));
        }
        if (visitors.isEmpty()) {
            throw new ObjectNotFoundException("Don't find any visitors of this event");
        }
        return visitors;
    }

    public Event createEvent(EventRequestDto eventRequestDto) {
        adminOfPlaceCheck(eventRequestDto.getPlaceID());
        Event event = DtoMapper.fromEventRequestDtoToEvent(eventRequestDto);
        EventDbDto eventDbDto = DtoMapper.fromEventToEventDbDto(event);
        return DtoMapper.fromEventDbDtoToEvent(eventRepository.save(eventDbDto));
    }

    public Event updateEvent(EventRequestDto eventRequestDto) {
        eventRepository.findById(eventRequestDto.getId()).orElseThrow(
                () -> new ObjectNotFoundException("Event with id " + eventRequestDto.getId() + " not found"));
        adminOfPlaceCheck(eventRequestDto.getPlaceID());
        Event event = DtoMapper.fromEventRequestDtoToEvent(eventRequestDto);
        EventDbDto eventDbDto = DtoMapper.fromEventToEventDbDto(event);
        return DtoMapper.fromEventDbDtoToEvent(eventRepository.save(eventDbDto));
    }


    @Transactional
    public void deleteEvent(int id) {
        EventDbDto eventDbDto = eventRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Event with id " + id + " not found"));
        adminOfPlaceCheck(eventDbDto.getPlace().getId());
        eventRepository.deleteById(id);
    }

    @Transactional
    public void deletePastEvents() {
        int result = eventRepository.deletePastEvents();
        if (result == 0) {
            throw new ObjectNotFoundException("don't find pasts events");
        }
    }

    public void adminOfPlaceCheck(int placeId) {
        User user = userService.findUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!Objects.equals(user.getRole(), "admin")) {
            if (user.getId() != placeService.getAdminOfPlace(placeId)) {
                throw new ForbiddenContentException("You are not admin of this place");
            }
        }
    }
}
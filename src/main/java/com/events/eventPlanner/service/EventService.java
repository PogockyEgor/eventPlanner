package com.events.eventPlanner.service;

import com.events.eventPlanner.domain.DTO.EventDbDto;
import com.events.eventPlanner.domain.DTO.EventRequestDto;
import com.events.eventPlanner.domain.Event;
import com.events.eventPlanner.domain.Place;
import com.events.eventPlanner.repository.EventRepository;
import com.events.eventPlanner.repository.PlaceRepository;
import com.events.eventPlanner.utils.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class EventService {
    EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event getEventById(int id) {
        return DtoMapper.fromEventDbDtoToEvent(eventRepository.findById(id).orElse(null));
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

    @Transactional
    public void deleteEvent(int id){
        eventRepository.deleteById(id);
    }
}

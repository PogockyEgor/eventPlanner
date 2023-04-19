package com.events.eventPlanner.utils;

import com.events.eventPlanner.domain.DTO.EventDbDto;
import com.events.eventPlanner.domain.DTO.EventRequestDto;
import com.events.eventPlanner.domain.DTO.PlaceDbDto;
import com.events.eventPlanner.domain.Event;
import com.events.eventPlanner.domain.Place;
import com.events.eventPlanner.repository.PlaceRepository;
import com.events.eventPlanner.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;

@Component
public class DtoMapper {

    static PlaceRepository placeRepository;

    @Autowired
    public DtoMapper(PlaceRepository placeRepository) {
        DtoMapper.placeRepository = placeRepository;
    }

    public static EventDbDto fromEventToEventDbDto(Event event){
        EventDbDto eventDbDto = new EventDbDto();
        eventDbDto.setId(event.getId());
        eventDbDto.setName(event.getName());
        eventDbDto.setDescription(event.getDescription());
        eventDbDto.setDate(event.getDate());
        PlaceDbDto placeDbDto = new PlaceDbDto();
        placeDbDto.setId(event.getPlace().getId());
        placeDbDto.setDescription(event.getPlace().getDescription());
        placeDbDto.setDistrict(event.getPlace().getDistrict());
        placeDbDto.setAddress(event.getPlace().getAddress());
        placeDbDto.setRating(event.getPlace().getRating());
        placeDbDto.setEvents(null);
        eventDbDto.setPlace(placeDbDto);
        return eventDbDto;
    }

    public static PlaceDbDto fromPlaceToPlaceDbDto(Place place){
        PlaceDbDto placeDbDto = new PlaceDbDto();
        placeDbDto.setId(place.getId());
        placeDbDto.setAddress(place.getAddress());
        placeDbDto.setDistrict(place.getDistrict());
        placeDbDto.setDescription(place.getDescription());
        placeDbDto.setRating(place.getRating());
        HashSet<EventDbDto> eventDbDtos = new HashSet<>();
        for (Event e : place.getEvents()) {
            EventDbDto edd = new EventDbDto();
            edd.setId(e.getId());
            edd.setName(e.getName());
            edd.setDescription(e.getDescription());
            edd.setDate(e.getDate());
            edd.setPlace(null);
            eventDbDtos.add(edd);
        }
        placeDbDto.setEvents(eventDbDtos);
        return placeDbDto;
    }

    public static Event fromEventDbDtoToEvent(EventDbDto eventDbDto){
        Event event = new Event();
        event.setId(eventDbDto.getId());
        event.setName(eventDbDto.getName());
        event.setDescription(eventDbDto.getDescription());
        event.setDate(eventDbDto.getDate());
        Place place = new Place();
        place.setId(eventDbDto.getPlace().getId());
        place.setAddress(eventDbDto.getPlace().getAddress());
        place.setDescription(eventDbDto.getPlace().getDescription());
        place.setDistrict(eventDbDto.getPlace().getDistrict());
        place.setRating(eventDbDto.getPlace().getRating());
        place.setEvents(null);
        event.setPlace(place);
        return event;
    }

    public static Place fromPlaceDbDtoToPlace(PlaceDbDto placeDbDto){
        Place place = new Place();
        place.setId(placeDbDto.getId());
        place.setAddress(placeDbDto.getAddress());
        place.setDistrict(placeDbDto.getDistrict());
        place.setDescription(placeDbDto.getDescription());
        place.setRating(placeDbDto.getRating());
        HashSet<Event> events = new HashSet<Event>();
        for (EventDbDto edd: placeDbDto.getEvents()) {
        Event event = new Event();
        event.setId(edd.getId());
        event.setDate(edd.getDate());
        event.setDescription(edd.getDescription());
        event.setName(edd.getName());
        event.setPlace(null);
        events.add(event);
        }
        place.setEvents(events);
        return place;
    }

    public static Event fromEventRequestDtoToEvent(EventRequestDto eventRequestDto){
        Event event = new Event();
        event.setId(eventRequestDto.getId());
        event.setName(eventRequestDto.getName());
        event.setDescription(eventRequestDto.getDescription());
        event.setDate(eventRequestDto.getDate());
        PlaceDbDto placeDbDto = placeRepository.findById(eventRequestDto.getPlaceID()).get();
        Place place = new Place();
        place.setId(placeDbDto.getId());
        place.setAddress(placeDbDto.getAddress());
        place.setDistrict(placeDbDto.getDistrict());
        place.setDescription(placeDbDto.getDescription());
        place.setRating(placeDbDto.getRating());
        place.setEvents(null);
        event.setPlace(place);
        return event;
    }
}
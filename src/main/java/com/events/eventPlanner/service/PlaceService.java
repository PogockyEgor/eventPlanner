package com.events.eventPlanner.service;

import com.events.eventPlanner.domain.DTO.PlaceDbDto;
import com.events.eventPlanner.domain.Place;
import com.events.eventPlanner.domain.User;
import com.events.eventPlanner.exceptions.ForbiddenContentException;
import com.events.eventPlanner.exceptions.ObjectNotFoundException;
import com.events.eventPlanner.mappers.DtoMapper;
import com.events.eventPlanner.repository.PlaceRepository;
import com.events.eventPlanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class PlaceService {

    PlaceRepository placeRepository;
    UserRepository userRepository;

    @Autowired
    public PlaceService(PlaceRepository placeRepository, UserRepository userRepository) {
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
    }

    public Place getPlaceById(int id) {
        Place place = DtoMapper.fromPlaceDbDtoToPlace(placeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Don't find place with id: " + id)));
        return place;
    }


    public ArrayList<Place> getAllPlaces() {
        ArrayList<PlaceDbDto> placesDbDto = (ArrayList<PlaceDbDto>) placeRepository.findAll();
        ArrayList<Place> places = new ArrayList<>();
        for (PlaceDbDto p : placesDbDto) {
            places.add(DtoMapper.fromPlaceDbDtoToPlace(p));
        }
        if (places.isEmpty()) {
            throw new ObjectNotFoundException("Don't find any places");
        }
        return places;
    }

    public Place createPlace(Place place) {
        PlaceDbDto placeDbDto = DtoMapper.fromPlaceToPlaceDbDto(place);
        return DtoMapper.fromPlaceDbDtoToPlace(placeRepository.save(placeDbDto));
    }

    public Place updatePlace(Place place) {
        placeRepository.findById(place.getId()).orElseThrow(
                () -> new ObjectNotFoundException("Don't find user with id: " + place.getId()));
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByLogin(login).orElseThrow(
                ()-> new ObjectNotFoundException("Don't find user in secure context"));
        if (!Objects.equals(user.getRole(), "admin")) {
            if (user.getId() != getAdminOfPlace(place.getId())) {
                throw new ForbiddenContentException("You are not admin of this place");
            }
        }
        PlaceDbDto placeDbDto = DtoMapper.fromPlaceToPlaceDbDto(place);
        return DtoMapper.fromPlaceDbDtoToPlace(placeRepository.save(placeDbDto));
    }

    @Transactional
    public void appointAdmin(int userId, int placeId) {
        placeRepository.findById(placeId).orElseThrow(
                () -> new ObjectNotFoundException("Don't find place with id: " + placeId));
        userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Don't find user with id: " + userId));
        placeRepository.appointAdmin(userId, placeId);
        userRepository.setUserPlaceAdmin(userId);
    }

    public void deletePlace(int id) {
        placeRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Don't find place with id: " + id));
        placeRepository.deleteById(id);
    }

    public int getAdminOfPlace(int placeId) {
        return placeRepository.getAdminOfPlace(placeId);
    }
}
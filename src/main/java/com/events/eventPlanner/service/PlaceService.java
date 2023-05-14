package com.events.eventPlanner.service;

import com.events.eventPlanner.domain.DTO.PlaceDbDto;
import com.events.eventPlanner.domain.Place;
import com.events.eventPlanner.mappers.DtoMapper;
import com.events.eventPlanner.repository.PlaceRepository;
import com.events.eventPlanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

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
        return DtoMapper.fromPlaceDbDtoToPlace(placeRepository.findById(id).orElseThrow());
    }

    public int getAdminOfPlace(int placeId) {
        return placeRepository.getAdminOfPlace(placeId);
    }

    public ArrayList<Place> getAllPlaces() {
        ArrayList<PlaceDbDto> placeDbDtos = (ArrayList<PlaceDbDto>) placeRepository.findAll();
        ArrayList<Place> places = new ArrayList<>();
        for (PlaceDbDto p : placeDbDtos) {
            places.add(DtoMapper.fromPlaceDbDtoToPlace(p));
        }
        return places;
    }

    public Place createPlace(Place place) {
        PlaceDbDto placeDbDto = DtoMapper.fromPlaceToPlaceDbDto(place);
        return DtoMapper.fromPlaceDbDtoToPlace(placeRepository.save(placeDbDto));
    }

    public Place updatePlace(Place place) {
        PlaceDbDto placeDbDto = DtoMapper.fromPlaceToPlaceDbDto(place);
        return DtoMapper.fromPlaceDbDtoToPlace(placeRepository.save(placeDbDto));
    }

    @Transactional
    public void appointAdmin(int userId, int placeId) {
        placeRepository.appointAdmin(userId, placeId);
        userRepository.setUserPlaceAdmin(userId);
    }

    @Transactional
    public void deletePlace(int id) {
        placeRepository.deleteById(id);
    }
}
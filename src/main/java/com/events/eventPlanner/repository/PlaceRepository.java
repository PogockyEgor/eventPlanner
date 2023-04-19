package com.events.eventPlanner.repository;

import com.events.eventPlanner.domain.DTO.PlaceDbDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<PlaceDbDto, Integer> {
}
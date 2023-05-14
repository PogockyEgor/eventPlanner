package com.events.eventPlanner.repository;

import com.events.eventPlanner.domain.DTO.PlaceDbDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<PlaceDbDto, Integer> {

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE place SET admin_id =:userId WHERE id=:eventId")
    void appointAdmin(int userId, int eventId);

    @Query(nativeQuery = true, value = "SELECT admin_id FROM place WHERE id=:placeId")
    int getAdminOfPlace(int placeId);
}
package com.events.eventPlanner.repository;

import com.events.eventPlanner.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE users SET is_deleted =true WHERE id = :id", countQuery = "SELECT * from users WHERE id = :id")
    void deleteUser(Integer id);

    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO l_users_events(event_id, user_id, create_time) VALUES (:eventID, :userID, :createTime)")
    int addEventToUser(int eventID, int userID, Date createTime);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM l_users_events WHERE event_id=:eventID and user_id=:userID")
    int deleteEventFromUser(int eventID, int userID);
}

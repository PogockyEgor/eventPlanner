package com.events.eventPlanner.repository;

import com.events.eventPlanner.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;

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

    @Query(nativeQuery = true, value = "SELECT users.id, name, login, password, birthday_date, created, email, edited, is_deleted, role FROM users INNER JOIN l_users_events lue on users.id = lue.user_id WHERE event_id=:eventId")
    ArrayList<User> getAllUsersForEvent(int eventId);

    Optional<User> findByLogin(String login);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE users SET role = 'placeAdmin' WHERE id =:userId")
    void setUserPlaceAdmin(int userId);
}
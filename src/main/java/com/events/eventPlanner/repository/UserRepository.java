package com.events.eventPlanner.repository;

import com.events.eventPlanner.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE users SET is_deleted =true WHERE id = :id", countQuery = "SELECT * from users WHERE id = :id")
    void deleteUser(Integer id);
}

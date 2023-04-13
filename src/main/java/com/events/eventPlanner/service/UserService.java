package com.events.eventPlanner.service;

import com.events.eventPlanner.domain.User;
import com.events.eventPlanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;

@Service
public class UserService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElse(new User());
    }

    public ArrayList<User> getAllUsers() {
        return (ArrayList<User>) userRepository.findAll();
    }

    public User createUser(User user) {
        user.setCreated(new Date(System.currentTimeMillis()));
        user.setEdited(new Date(System.currentTimeMillis()));
        return userRepository.save(user);
    }

    public User updateUser(User user){
        user.setEdited(new Date(System.currentTimeMillis()));
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(int id){
        userRepository.deleteUser(id);
    }
}

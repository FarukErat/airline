package com.example.airline.service;

import com.example.airline.model.UserDetails;
import com.example.airline.repository.UserRepoistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepoistory userRepoistory;

    public List<UserDetails> getAllUsers() {
        return userRepoistory.findAll();
    }

    public Optional<UserDetails> getUserById(Long id) {
        return userRepoistory.findById(id);
    }

    public UserDetails saveUser(UserDetails user) {
        return userRepoistory.save(user);
    }

    public void deleteUser(Long id) {
        userRepoistory.deleteById(id);
    }
}

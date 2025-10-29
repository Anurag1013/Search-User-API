package com.example.usersbackend.service;

import com.example.usersbackend.dto.UserResponse;
import com.example.usersbackend.model.User;
import java.util.List;

/**
 * Interface for user service.
 */
public interface UserService {
    List<User> loadFromRemote();
    List<UserResponse> getAll();
    List<User> search(String query);
    User findById(Long id);
    User findByEmail(String email);
    User createUser(User user);
}

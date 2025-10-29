package com.example.usersbackend.controller;

import com.example.usersbackend.dto.UserResponse;
import com.example.usersbackend.model.User;
import com.example.usersbackend.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class is responsible for handling requests related to the User resource.
 * It provides endpoints for loading users from a remote source, retrieving all users, searching for users by query,
 * retrieving a user by ID, retrieving a user by email, and creating a new user.
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService){ this.userService = userService; }

    /**
     * Loads users from a remote source.
     * @return the response containing the list of users
     */
    @PostMapping("/load")
    public ResponseEntity<List<User>> load(){ return ResponseEntity.ok(userService.loadFromRemote()); }

    /**
     * Retrieves all users.
     * @return the response containing the list of users
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> all(){ return ResponseEntity.ok(userService.getAll()); }

    /**
     * Searches for users by query.
     * @param query the search query
     * @return the response containing the list of matching users
     */
    @GetMapping("/search")
    public ResponseEntity<List<User>> search(@RequestParam("query") String query) {
        if (query == null || query.trim().length() < 3) {
            throw new IllegalArgumentException("Query must be at least 3 characters long");
        }
        return ResponseEntity.ok(userService.search(query));
    }

    /**
     * Retrieves a user by ID.
     * @param id the ID of the user
     * @return the response containing the user with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    /**
     * Retrieves a user by email.
     * @param email the email of the user
     * @return the response containing the user with the specified email
     */
    @GetMapping("/by-email")
    public ResponseEntity<User> getByEmail(@RequestParam @Email String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    /**
     * Creates a new user.
     * @param user the user to create
     * @return the response containing the created user
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        return ResponseEntity.ok(userService.createUser(user)); }
}

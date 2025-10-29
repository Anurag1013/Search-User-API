package com.example.usersbackend.service;

import com.example.usersbackend.dto.DummyUserResponse;
import com.example.usersbackend.dto.UserResponse;
import com.example.usersbackend.exception.ResourceNotFoundException;
import com.example.usersbackend.model.User;
import com.example.usersbackend.repository.UserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides an implementation of the UserService interface for user management.
 * It interacts with the UserRepository bean to perform CRUD operations on the User entity.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${external.api.users:https://dummyjson.com/users}")
    private String dummyBaseUrl;

    public UserServiceImpl(UserRepository userRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    /**
     * Fetches user data from a remote API and saves it to the database.
     * @return a list of user objects
     */
    @Override
    @Transactional
    @Retry(name = "dummyApiRetry")
    @CircuitBreaker(name = "dummyApiCB", fallbackMethod = "loadFallback")
    public List<User> loadFromRemote() {
        log.info("Fetching users from {}", dummyBaseUrl);

        try {
            DummyUserResponse resp = restTemplate.getForObject(dummyBaseUrl, DummyUserResponse.class);

            if (resp == null || resp.getUsers() == null) {
                log.error("No data returned from remote API");
                throw new RuntimeException("No data returned from remote API");
            }

            List<User> users = resp.getUsers().stream()
                    .map(this::mapToUser)
                    .collect(Collectors.toList());

            userRepository.deleteAll();
            userRepository.saveAll(users);

            log.info("Loaded {} users into H2 database successfully", users.size());
            return users;

        } catch (Exception ex) {
            log.error("Error while fetching from remote API: {}", ex.getMessage());
            throw new RuntimeException("Remote API call failed", ex);
        }
    }

    /**
     * Fallback method if circuit breaker trips.
     */
    public List<User> loadFallback(Throwable throwable) {
        log.warn("Fallback triggered for loadFromRemote(): {}", throwable.getMessage());
        return userRepository.findAll(); // Return existing data from DB if available
    }

    /**
     * Saves a new user to the database.
     * @param user the user object to save
     * @return the saved user object
     */
    @Override
    public User createUser(User user) {

        if (user.getId() != null && userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("User with ID " + user.getId() + " already exists");
        }

        if (StringUtils.isNotBlank(user.getEmail())&& userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
        }

        log.info("Creating new user: {} {}", user.getFirstName(), user.getLastName());
        return userRepository.save(user);
    }

    /**
     * Retrieves all users from the database.
     * @return a list of user objects
     */
    @Override
    public List<UserResponse> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserResponse(user))
                .collect(Collectors.toList());
    }

    /**
     * Searches for users in the database based on a query string.
     * @param query the query string to search for
     * @return a list of user objects that match the query
     */
    @Override
    public List<User> search(String query) {
        String qlow = query == null ? "" : query.toLowerCase();
        return userRepository.freeTextSearch(qlow);
    }

    /**
     * Retrieves a user from the database by ID.
     * @param id the ID of the user to retrieve
     * @return the user object
     */
    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    /**
     * Retrieves a user from the database by email.
     * @param email the email of the user to retrieve
     * @return the user object
     */
    @Override
    public User findByEmail(String email) {
        User u = userRepository.findByEmail(email);
        if (u == null) throw new ResourceNotFoundException("User not found for email: " + email);
        return u;
    }

    /**
     * Maps a DummyUserResponse.DummyUser object to a User object.
     * @param dummyUser the DummyUserResponse.DummyUser object to map
     * @return the mapped User object
     */
    private User mapToUser(DummyUserResponse.DummyUser dummyUser) {
        String addressJson = dummyUser.getAddress() != null ? dummyUser.getAddress().toString() : null;
        return User.builder()
                .id(dummyUser.getId() == null ? null : dummyUser.getId().longValue())
                .firstName(dummyUser.getFirstName())
                .lastName(dummyUser.getLastName())
                .email(dummyUser.getEmail())
                .age(dummyUser.getAge())
                .phone(dummyUser.getPhone())
                .username(dummyUser.getUsername())
                .gender(dummyUser.getGender())
                .ssn(dummyUser.getSsn())
                .role("User")
                .addressJson(addressJson)
                .build();
    }
}

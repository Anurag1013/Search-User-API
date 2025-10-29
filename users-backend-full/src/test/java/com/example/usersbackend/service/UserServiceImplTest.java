package com.example.usersbackend.service;

import com.example.usersbackend.dto.DummyUserResponse;
import com.example.usersbackend.dto.UserResponse;
import com.example.usersbackend.exception.ResourceNotFoundException;
import com.example.usersbackend.model.User;
import com.example.usersbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<UserResponse> result = userService.getAll();
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Success() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testFindById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void testFindByEmail_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        User result = userService.findByEmail("test@example.com");
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testFindByEmail_NotFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> userService.findByEmail("missing@example.com"));
    }

    @Test
    void testSearchUsers() {
        String query = "john";
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.freeTextSearch("john")).thenReturn(users);

        List<User> result = userService.search(query);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).freeTextSearch("john");
    }

    @Test
    void testCreateUser_Success() {
        User user = new User();
        user.setEmail("unique@example.com");
        user.setFirstName("John");

        when(userRepository.findByEmail("unique@example.com")).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        User saved = userService.createUser(user);
        assertEquals("unique@example.com", saved.getEmail());
    }

    @Test
    void testCreateUser_DuplicateEmail() {
        User existing = new User();
        existing.setEmail("dup@example.com");

        when(userRepository.findByEmail("dup@example.com")).thenReturn(existing);

        User newUser = new User();
        newUser.setEmail("dup@example.com");

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(newUser));
    }

    @Test
    void testLoadFromRemote_NoData() {
        when(restTemplate.getForObject(anyString(), eq(DummyUserResponse.class))).thenReturn(null);
        assertThrows(RuntimeException.class, () -> userService.loadFromRemote());
    }
}

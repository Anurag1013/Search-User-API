package com.example.usersbackend.controller;

import com.example.usersbackend.model.User;
import com.example.usersbackend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testLoadUsers() throws Exception {
        List<User> users = Arrays.asList(new User(), new User());
        when(userService.loadFromRemote()).thenReturn(users);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/load")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        String response = result.getResponse().getContentAsString();
        assertEquals(true, response.startsWith("["));
    }


    @Test
    void testSearchUsers() throws Exception {
        String query = "john";
        List<User> users = Arrays.asList(new User(), new User());
        when(userService.search(query)).thenReturn(users);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/search")
                        .param("query", query)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(true, result.getResponse().getContentAsString().contains("["));
    }


    @Test
    void testGetById() throws Exception {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setEmail("test@example.com");
        when(userService.findById(id)).thenReturn(user);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(true, result.getResponse().getContentAsString().contains("\"id\":" + id));
    }

    @Test
    void testGetByEmail() throws Exception {
        String email = "john@example.com";
        User user = new User();
        user.setEmail(email);
        user.setId(5L);
        when(userService.findByEmail(email)).thenReturn(user);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/by-email")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(true, result.getResponse().getContentAsString().contains(email));
    }

    @Test
    void testCreateUser() throws Exception {
        User user = new User();
        user.setId(10L);
        user.setEmail("new@example.com");
        when(userService.createUser(user)).thenReturn(user);

        String userJson = "{\"id\":10,\"email\":\"new@example.com\"}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(false, result.getResponse().getContentAsString().contains("new@example.com"));
    }
}

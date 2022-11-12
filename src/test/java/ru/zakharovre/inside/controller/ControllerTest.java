package ru.zakharovre.inside.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.zakharovre.inside.dto.AuthRequest;
import ru.zakharovre.inside.dto.MessageRequestDto;
import ru.zakharovre.inside.entity.Role;
import ru.zakharovre.inside.entity.User;
import ru.zakharovre.inside.repository.RoleRepository;
import ru.zakharovre.inside.service.MessageService;
import ru.zakharovre.inside.service.UserService;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

    private final static String LOGIN_PATH = "/api/login";
    private final static String MESSAGE_PATH = "/api/message";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserService userService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    MessageService messageService;

    User user = new User();
    MessageRequestDto message = new MessageRequestDto();

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setRole("ADMIN");
        roleRepository.save(role);
        user.setUsername("user");
        user.setPassword("password");
        user.setRoles(new HashSet<>(Collections.singleton(role)));
        userService.addUser(user);
        message.setUsername("user");
        message.setMessage("message");
    }

    @Test
    public void failAuthTest() throws Exception {
        byte[] content = objectMapper.writeValueAsBytes(message);
        mockMvc.perform(post(MESSAGE_PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testPostMessage() throws Exception {
        assertEquals(0, messageService.findAll().size());
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(user.getUsername());
        authRequest.setPassword(user.getPassword());
        byte[] content = objectMapper.writeValueAsBytes(authRequest);
        MvcResult result = mockMvc.perform(post(LOGIN_PATH).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String token = JsonPath.read(result.getResponse().getContentAsString(), "$.token");
        content = objectMapper.writeValueAsBytes(message);
        mockMvc.perform(post(MESSAGE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer_" + token).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")));
        assertEquals(1, messageService.findAll().size());
    }

    @AfterEach
    public void tearDown() {
        userService.deleteAll();
        messageService.deleteAll();
        roleRepository.deleteAll();
    }
}

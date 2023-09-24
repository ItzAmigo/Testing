package com.Itzamigo.SQATProject.controller;

import com.Itzamigo.SQATProject.entity.User;
import com.Itzamigo.SQATProject.repository.UserRepository;
import com.Itzamigo.SQATProject.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.OK;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private static HttpHeaders headers;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @Sql(statements = "INSERT INTO users(id, nickname, email) VALUES (22, 'n10', 'e10')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM users WHERE id='22'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testUsersList() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<List<User>> response = restTemplate.exchange(
                createURLWithPort(), HttpMethod.GET, entity, new ParameterizedTypeReference<List<User>>(){});
        List<User> userList = response.getBody();
        assert userList != null;
        assertEquals(response.getStatusCodeValue(), 200);
        assertEquals(userList.size(), userService.getUsers().size());
        assertEquals(userList.size(), userRepository.findAll().size());
    }

    @Test
    @Sql(statements = "INSERT INTO users(id, nickname, email) VALUES (24, 'n11', 'e11')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM users WHERE id='24'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testUserById() throws JsonProcessingException {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<User> response = restTemplate.exchange(
                (createURLWithPort() + "/24"), HttpMethod.GET, entity, User.class);
        User userRes = response.getBody();
        String expected = "{\"id\":24,\"nickname\":\"n11\",\"email\":\"e11\"}";
        assertEquals(response.getStatusCode(), OK);
        assertEquals(expected, objectMapper.writeValueAsString(userRes));
        assert userRes != null;
        assertEquals(userRes, userService.getUserById(24L));
        assertEquals(userRes.getNickname(), userService.getUserById(24L).getNickname());
        assertEquals(userRes, userRepository.findById(24L).orElse(null));
    }

    @Test
    @Sql(statements = "DELETE FROM users WHERE id='3'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testCreateUser() throws JsonProcessingException {
        User user = new User(3L, "peter", "p");
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(user), headers);
        ResponseEntity<User> response = restTemplate.exchange(
                createURLWithPort(), HttpMethod.POST, entity, User.class);
        assertEquals(response.getStatusCodeValue(), 201);
        User userRes = Objects.requireNonNull(response.getBody());
        assertEquals(userRes.getNickname(), "peter");
        assertEquals(userRes.getNickname(), userRepository.save(user).getNickname());
    }

    @Test
    @Sql(statements = "INSERT INTO users(id, nickname, email) VALUES (6, 'nameee', 'emaaail')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM users WHERE id='6'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testDeleteUser() {
        ResponseEntity<String> response = restTemplate.exchange(
                (createURLWithPort() + "/6"), HttpMethod.DELETE, null, String.class);
        String userRes = response.getBody();
        assertEquals(response.getStatusCodeValue(), 200);
        assertNotNull(userRes);
        assertEquals(userRes, "User deleted - User ID:6");
    }

    private String createURLWithPort() {
        return "http://localhost:" + port + "/api/users";
    }

}

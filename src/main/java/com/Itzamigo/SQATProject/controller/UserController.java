package com.Itzamigo.SQATProject.controller;

import com.Itzamigo.SQATProject.entity.User;
import com.Itzamigo.SQATProject.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class UserController {

    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping(path = "/users")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User newUser = userService.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping(path = "/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    @DeleteMapping(path = "/users/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        boolean deleteUserById = userService.deleteUserById(id);
        if (deleteUserById) {
            return new ResponseEntity<>(("User deleted - User ID:" + id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(("User deletion failed - User ID:" + id), HttpStatus.BAD_REQUEST);
        }
    }
}
package com.Itzamigo.SQATProject.service;

import com.Itzamigo.SQATProject.entity.User;
import com.Itzamigo.SQATProject.exception.UserNotFoundException;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    User getUserById(Long id);

    boolean deleteUserById(Long id);

    User createUser(User user);

    UserNotFoundException throwException(String value);
}
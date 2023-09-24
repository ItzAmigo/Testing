package com.Itzamigo.SQATProject.service;

import com.Itzamigo.SQATProject.entity.User;
import com.Itzamigo.SQATProject.exception.UserNotFoundException;
import com.Itzamigo.SQATProject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> throwException(String.valueOf(id)));
    }

    @Override
    public boolean deleteUserById(Long id) {
        Optional<User> order = userRepository.findById(id);
        if (order.isPresent()) {
            userRepository.deleteById(id);
            return true;
        } else {
            throwException(String.valueOf(id));
            return false;
        }
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserNotFoundException throwException(String value) {
        throw new UserNotFoundException("User Not Found with ID: " + value);
    }


}
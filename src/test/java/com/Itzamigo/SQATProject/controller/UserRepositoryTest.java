package com.Itzamigo.SQATProject.controller;

import com.Itzamigo.SQATProject.entity.User;
import com.Itzamigo.SQATProject.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.save(new User(100L, "Arnur", "arnur.sovetkali@gmail.com"));
        userRepository.save(new User(200L, "Minato", "yellow_flash_of_konoha@gmail.com"));
    }

    @AfterEach
    public void destroy() {
        userRepository.deleteAll();
    }


    @Test
    public void testGetAllUsers() {
        List<User> userList = userRepository.findAll();
        Assertions.assertThat(userList.size()).isEqualTo(2);
        Assertions.assertThat(userList.get(0).getId()).isNotNegative();
        Assertions.assertThat(userList.get(0).getId()).isGreaterThan(0);
        Assertions.assertThat(userList.get(0).getNickname()).isEqualTo("Arnur");
    }

    @Test
    public void testGetInvalidUser() {
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            userRepository.findById(120L).get();
        });
        Assertions.assertThat(exception).isNotNull();
        Assertions.assertThat(exception.getClass()).isEqualTo(NoSuchElementException.class);
        Assertions.assertThat(exception.getMessage()).isEqualTo("No value present");
    }

    @Test
    public void testGetCreateUser() {
        User saved = new User(300L, "Name",  "email");
        User returned = userRepository.save(saved);
        Assertions.assertThat(returned).isNotNull();
        Assertions.assertThat(returned.getNickname()).isNotEmpty();
        Assertions.assertThat(returned.getId()).isGreaterThan(1);
        Assertions.assertThat(returned.getId()).isNotNegative();
        Assertions.assertThat(saved.getNickname()).isEqualTo(returned.getNickname());
    }

    @Test
    public void testDeleteUser() {
        User saved = new User(400L, "n1", "e1");
        userRepository.save(saved);
        userRepository.delete(saved);
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            userRepository.findById(400L).get();
        });
        Assertions.assertThat(exception).isNotNull();
        Assertions.assertThat(exception.getClass()).isEqualTo(NoSuchElementException.class);
        Assertions.assertThat(exception.getMessage()).isEqualTo("No value present");
    }
}
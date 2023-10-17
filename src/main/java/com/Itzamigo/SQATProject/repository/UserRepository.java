package com.Itzamigo.SQATProject.repository;

import com.Itzamigo.SQATProject.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Autowired
    @Qualifier("firstJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

}

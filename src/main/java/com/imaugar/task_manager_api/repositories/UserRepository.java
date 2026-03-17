package com.imaugar.task_manager_api.repositories;

import com.imaugar.task_manager_api.entities.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //Encontrar por username
    Optional<User> findByUsername(String username);
    //Encontrar por email
    Optional<User> findByEmail(String email);
}

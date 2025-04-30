package com.weebeeio.demo.domain.login.repository;

import com.weebeeio.demo.domain.login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findById(String id);
    boolean existsById(String id);
    Optional<User> findByIdAndPassword(String id, String password);
} 
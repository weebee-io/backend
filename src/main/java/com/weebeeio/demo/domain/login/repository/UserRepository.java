package com.weebeeio.demo.domain.login.repository;

import com.weebeeio.demo.domain.login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findById(String id);
    
    boolean existsById(String id);

    boolean existsByNickname(String nickname);
}
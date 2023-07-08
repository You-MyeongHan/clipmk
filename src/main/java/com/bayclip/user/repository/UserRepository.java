package com.bayclip.security.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bayclip.security.user.entity.User;


public interface UserRepository extends JpaRepository<User, Integer>{
    public Optional<User> findByUid(String uid);
    public Optional<User> findById(Long id);
    public boolean existsByUid(String uid);
    public boolean existsByEmail(String email);
}
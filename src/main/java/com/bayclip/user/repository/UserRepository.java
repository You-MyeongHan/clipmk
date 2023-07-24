package com.bayclip.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bayclip.user.entity.User;


public interface UserRepository extends JpaRepository<User, Integer>{
    public Optional<User> findByUid(String uid);
    public Optional<User> findById(Integer id);
    public boolean existsByUid(String uid);
    public boolean existsByNick(String nick);
}
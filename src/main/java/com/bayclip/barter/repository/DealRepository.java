package com.bayclip.barter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bayclip.barter.entity.Deal;

public interface DealRepository extends JpaRepository<Deal, Long>{

}

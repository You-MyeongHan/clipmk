package com.clipmk.barter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.clipmk.barter.entity.Deal;

public interface DealRepository extends JpaRepository<Deal, Long>{
	Page<Deal> findByToItemId(long id, Pageable pageable);
	Page<Deal> findByToUserId(int id, Pageable pageable);
	Page<Deal> findByFromUserId(int id, Pageable pageable);
}

package com.bayclip.barter.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.bayclip.barter.entity.Item;

public interface BarterRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item>{
	Optional<Item> findItemById(Long itemId);
	Page<Item> findByTitleContaining(Pageable pageable, String searchTerm);
	Page<Item> findByUserId(int userId, Pageable pageable);
}

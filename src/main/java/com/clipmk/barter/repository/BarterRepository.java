package com.clipmk.barter.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.clipmk.barter.entity.Item;

public interface BarterRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item>{
	Optional<Item> findItemById(long itemId);
	Page<Item> findByTitleContaining(Pageable pageable, String searchTerm);
	Page<Item> findByUserId(int userId, Pageable pageable);
	@Query("SELECT i FROM Item i JOIN i.dib w WHERE w = :userId")
    Page<Item> findDibItemsByUserId(@Param("userId") int userId, Pageable pageable);
}

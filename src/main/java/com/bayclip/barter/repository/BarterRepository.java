package com.bayclip.barter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.bayclip.barter.entity.Item;
import com.bayclip.board.entity.Post;

public interface BarterRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item>{
	Item findItemById(Long itemId);
	Page<Item> findByTitleContaining(Pageable pageable, String searchTerm);
}

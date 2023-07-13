package com.bayclip.barter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bayclip.barter.entity.Item;

public interface BarterRepository extends JpaRepository<Item, Long>{
	Item findItemById(Long itemId);
}

package com.bayclip.barter.service;

import org.springframework.stereotype.Service;

import com.bayclip.barter.entity.Item;
import com.bayclip.barter.repository.BarterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BarterService {
	private final BarterRepository barterRepository;
	
	public Item getItemById(Long itemId) {
		Item item=barterRepository.findItemById(itemId);
		return item;
	}
}
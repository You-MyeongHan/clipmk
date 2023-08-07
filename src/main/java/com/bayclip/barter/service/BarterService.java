package com.bayclip.barter.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bayclip.barter.controller.ItemRequestDto;
import com.bayclip.barter.dto.EditItemRequestDto;
import com.bayclip.barter.dto.ItemDto;
import com.bayclip.barter.entity.Item;
import com.bayclip.barter.repository.BarterRepository;
import com.bayclip.user.entity.User;
import com.bayclip.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BarterService {
	private final BarterRepository barterRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public ItemDto getItemById(Long itemId) {
		Item item=barterRepository.findItemById(itemId);
		
		if(item!=null) {
			return item.toDto();
		}
		
		return null;
	}
	
	@Transactional
	public Boolean register(ItemRequestDto request, Integer userId) {
		
		User user=userRepository.findById(userId).orElseThrow(()->
		new IllegalStateException("존재하지 않는 계정입니다."));
		
		Item item = Item.builder()
				.title(request.getTitle())
				.category(request.getCategory())
				.content(request.getContent())
				.user(user)
				.build();
		
		barterRepository.save(item);
		
		return true;
	}
	
	@Transactional
	public ItemDto edit(Long itemId, EditItemRequestDto request, User user) {
		
		Item item= barterRepository.findById(itemId).orElse(null);
		
		if(user!=null) {
			String newContent=request.getContent();
			
			if(newContent != null) {
				item.setContent(newContent);
			}
			barterRepository.save(item);
			
			return getItemById(itemId);

		}
		
		return null;
	}
	
	@Transactional
	public boolean delete(Long itemId, User user) {
		
		Item item=barterRepository.findById(itemId).orElse(null);
		
		if(user!=null) {
			if(item!=null) {
				barterRepository.delete(item);
				return true;
			}
		}
		return false;
	}
}
package com.bayclip.barter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bayclip.barter.dto.DealRequestDto;
import com.bayclip.barter.dto.EditItemRequestDto;
import com.bayclip.barter.dto.ItemReqDto;
import com.bayclip.barter.dto.ItemResDto;
import com.bayclip.barter.entity.Deal;
import com.bayclip.barter.entity.Item;
import com.bayclip.barter.repository.BarterRepository;
import com.bayclip.barter.repository.DealRepository;
import com.bayclip.user.entity.User;
import com.bayclip.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BarterService {
	private final BarterRepository barterRepository;
	private final DealRepository dealRepository;
	private final UserRepository userRepository;
	
	// 아이템 가져오기
	@Transactional
	public ItemResDto getItemById(Long itemId) {
		Item item=barterRepository.findItemById(itemId);
		
		if(item!=null) {
			return item.toDto();
		}
		
		return null;
	}
	
	//아이템 등록
	@Transactional
	public Boolean register(ItemReqDto request, Integer userId) {
		
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
	
	//아이템 수정
	@Transactional
	public ItemResDto edit(Long itemId, EditItemRequestDto request, User user) {
		
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
	
	//아이템 삭제
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
	
	//아이템 페이징
	public Page<Item> findAll(Pageable pageable, String category){
		Specification<Item> spec = null;
		if(category !=null && !category.isEmpty()) {
			spec = (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("category"), category);
		}
		
		if (spec != null) {
            return barterRepository.findAll(spec, pageable);
        } else {
            return barterRepository.findAll(pageable);
        }
	}
	
	//아이템 검색
	public Page<Item> findByTitleContaining(Pageable pageable, String searchTerm){
		return barterRepository.findByTitleContaining(pageable, searchTerm);
	}
	
	//거래 제안
	public boolean suggestDeal(DealRequestDto request, User fromUser) {
		
		User toUser = userRepository.findById(request.getToUserId()).orElse(null);
		Item fromItem = barterRepository.findById(request.getFromItemId()).orElse(null);
		Item toItem = barterRepository.findById(request.getToItemId()).orElse(null);
		
		if(toUser !=null && fromUser !=null) {
			if(fromItem!=null && toItem!=null) {
				
				Deal deal= Deal.builder()
						.content(request.getContent())
						.fromItemId(fromItem)
						.toItemId(toItem)
						.build();
						
				dealRepository.save(deal);
				return true;
			}
		}
		
		return false;
	}
	
	//거래 아이디로 거래 조회
	public Deal getDealById(long dealId) {
		return dealRepository.findById(dealId).orElse(null);
	}
	
	public void dealOn(Deal deal) {
		deal.setStatus(1);
		dealRepository.save(deal);
	}
	
}
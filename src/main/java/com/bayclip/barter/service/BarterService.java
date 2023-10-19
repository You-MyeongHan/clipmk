package com.bayclip.barter.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bayclip.barter.dto.DealReqDto;
import com.bayclip.barter.dto.EditItemReqDto;
import com.bayclip.barter.dto.ItemReqDto;
import com.bayclip.barter.dto.ItemResDto;
import com.bayclip.barter.entity.Deal;
import com.bayclip.barter.entity.Item;
import com.bayclip.barter.repository.BarterRepository;
import com.bayclip.barter.repository.DealRepository;
import com.bayclip.user.entity.User;
import com.bayclip.user.repository.UserRepository;
import com.global.error.errorCode.BarterErrorCode;
import com.global.error.exception.RestApiException;

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
		Item item=barterRepository.findItemById(itemId).orElseThrow(
				()-> new RestApiException(BarterErrorCode.ITEM_NOT_FOUND));
		
		return item.toDto();
		
	}
	
	//아이템 등록
	@Transactional
	public Boolean register(ItemReqDto request, Integer userId) {
		
		User user=userRepository.findById(userId).orElse(null);
		
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
	public ItemResDto edit(Long itemId, EditItemReqDto request, User user) {
		
		Item item=barterRepository.findItemById(itemId).orElseThrow(
				()-> new RestApiException(BarterErrorCode.ITEM_NOT_FOUND));
		
		if (!item.getUser().getId().equals(user.getId())) {
	        throw new RestApiException(BarterErrorCode.ITEM_ACCESS_DENIED);
	    }
		
		item.setTitle(request.getTitle());
		item.setContent(request.getContent());
		item.setCategory(request.getCategory());
		item.setRe_date(LocalDateTime.now());
		barterRepository.save(item);
		
		return getItemById(itemId);

	}
	
	//아이템 삭제
	@Transactional
	public void delete(Long itemId, User user) {
		
		Item item=barterRepository.findItemById(itemId).orElseThrow(
				()-> new RestApiException(BarterErrorCode.ITEM_NOT_FOUND));
		
		if (!item.getUser().getId().equals(user.getId())) {
	        throw new RestApiException(BarterErrorCode.ITEM_ACCESS_DENIED);
	    }
		
		barterRepository.delete(item);
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
	public boolean suggestDeal(DealReqDto request, User fromUser) {
		
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
	
	public Page<Item> findByUserId(int userId, Pageable pageable){
		Page<Item> Items = barterRepository.findByUserId(userId, pageable);
		return Items;
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
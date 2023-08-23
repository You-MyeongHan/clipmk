package com.bayclip.barter.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bayclip.barter.dto.DealAcceptRequestDto;
import com.bayclip.barter.dto.DealRequestDto;
import com.bayclip.barter.dto.EditItemRequestDto;
import com.bayclip.barter.dto.ItemReqDto;
import com.bayclip.barter.dto.ItemResDto;
import com.bayclip.barter.dto.ItemsResDto;
import com.bayclip.barter.entity.Deal;
import com.bayclip.barter.service.BarterService;
import com.bayclip.chat.service.ChatService;
import com.bayclip.user.entity.User;
import com.bayclip.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/barter")
@RequiredArgsConstructor
public class BarterController {
	private final BarterService barterService;
	private final ChatService chatService;
	
	//아이템 등록
	@PostMapping("/item")
	public ResponseEntity<Void> register(
			@RequestBody ItemReqDto request,
			@AuthenticationPrincipal User user
	){
		if(barterService.register(request, user.getId())) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	//아이템 조회
	@GetMapping("/item/{itemId}")
	public ResponseEntity<ItemResDto> getItemById(@PathVariable("itemId") Long itemId){
		ItemResDto itemDto=barterService.getItemById(itemId);
		
		if(itemDto != null) {
			return ResponseEntity.ok(itemDto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//아이템 수정
	@PatchMapping("/item/{item-id}")
	public ResponseEntity<ItemResDto> editItem(
			@PathVariable("item-id") Long itemId,
			@RequestBody EditItemRequestDto request,
			@AuthenticationPrincipal User user
	){
		ItemResDto itemDto=barterService.edit(itemId, request, user);
		
		if(itemDto != null) {
			return ResponseEntity.ok(itemDto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//아이템 삭제
	@DeleteMapping("/item/{item-id}")
	public ResponseEntity<Void> deleteItem(
			@PathVariable("item-id") Long itemId,
			@AuthenticationPrincipal User user
	){	
		
		if(barterService.delete(itemId, user)) {
			return ResponseEntity.ok().build();
		}
		else{
			return ResponseEntity.notFound().build();
		}
	}
	
	//아이템 페이징
	@GetMapping("/items")
	public ResponseEntity<Page<ItemsResDto>> items(
			@PageableDefault(page=0, size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam(value="category", defaultValue = "") String category,
			@RequestParam(value = "searchTerm", defaultValue  = "") String searchTerm
			){
		Page<ItemsResDto> items=null;
		
		if(searchTerm.isEmpty()) {
			items=barterService.findAll(pageable, category).map(ItemsResDto::from);
		}else {
			items=barterService.findByTitleContaining(pageable, searchTerm).map(ItemsResDto::from);
		}
		
		return ResponseEntity.ok(items);
	}
	
	//거래 제안
	@PostMapping("/suggest")
	public ResponseEntity<Void> suggestDeal(
			@RequestBody DealRequestDto request,
			@AuthenticationPrincipal User user
	){
		
		if(barterService.suggestDeal(request, user)) {
			return ResponseEntity.ok().build();
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//거래 수락
	@PostMapping("/accept")
	public ResponseEntity<Long> acceptDeal(
			@RequestBody DealAcceptRequestDto request,
			@AuthenticationPrincipal User user
	){
		Deal deal = barterService.getDealById(request.getDealId());
		
		if(deal != null) {
			if(user.getId() == deal.getToItemId().getUser().getId()) {
				long chatRoomId = chatService.createChatRoom(deal);
				barterService.dealOn(deal);
				return ResponseEntity.ok(chatRoomId);
			}
			return ResponseEntity.badRequest().build();
			
		}
		
		return ResponseEntity.badRequest().build();
		
	}
	
	//거래 거절
	@PostMapping("/reject")
	public ResponseEntity<Void> rejectDeal(
			@AuthenticationPrincipal User user
	){
		return ResponseEntity.ok().build();
	}
	
	//거래 완료
	@PostMapping("/complete")
	public ResponseEntity<Void> completeDeal(
			
			@AuthenticationPrincipal User user
	){
		return ResponseEntity.ok().build();
	}
	
}

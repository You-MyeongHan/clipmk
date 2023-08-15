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

import com.bayclip.barter.dto.DealRequestDto;
import com.bayclip.barter.dto.EditItemRequestDto;
import com.bayclip.barter.dto.ItemDto;
import com.bayclip.barter.dto.ItemRequestDto;
import com.bayclip.barter.dto.ItemsResponseDto;
import com.bayclip.barter.service.BarterService;
import com.bayclip.user.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/barter")
@RequiredArgsConstructor
public class BarterController {
	private final BarterService barterService;
	
	//아이템 등록
	@PostMapping("/{itemId}")
	public ResponseEntity<Void> register(
			@RequestBody ItemRequestDto request,
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
	@GetMapping("/{itemId}")
	public ResponseEntity<ItemDto> getItemById(@PathVariable("itemId") Long itemId){
		ItemDto itemDto=barterService.getItemById(itemId);
		
		if(itemDto != null) {
			return ResponseEntity.ok(itemDto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//아이템 수정
	@PatchMapping("/{item-id}")
	public ResponseEntity<ItemDto> editItem(
			@PathVariable("item-id") Long itemId,
			@RequestBody EditItemRequestDto request,
			@AuthenticationPrincipal User user
	){
		ItemDto itemDto=barterService.edit(itemId, request, user);
		
		if(itemDto != null) {
			return ResponseEntity.ok(itemDto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//아이템 삭제
	@DeleteMapping("/{item-id}")
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
	public ResponseEntity<Page<ItemsResponseDto>> items(
			@PageableDefault(page=0, size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam(value="category", defaultValue = "") String category,
			@RequestParam(value = "searchTerm", defaultValue  = "") String searchTerm
			){
		Page<ItemsResponseDto> items=null;
		
		if(searchTerm.isEmpty()) {
			items=barterService.findAll(pageable, category).map(ItemsResponseDto::from);
		}else {
			items=barterService.findByTitleContaining(pageable, searchTerm).map(ItemsResponseDto::from);
		}
		
		return ResponseEntity.ok(items);
	}
	
	//거래 요청
	@PostMapping("/suggestDeal")
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
	@PostMapping("/accept/{deal-id}")
	public ResponseEntity<Void> acceptDeal(
			@PathVariable("deal-id") Long dealId,
			@AuthenticationPrincipal User user
	){
		return ResponseEntity.ok().build();
	}
	
	
}

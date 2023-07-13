package com.bayclip.barter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bayclip.barter.entity.Item;
import com.bayclip.barter.service.BarterService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/barter")
@RequiredArgsConstructor
public class BarterController {
	private final BarterService barterService;
	
	@GetMapping("/{itemId}")
	public ResponseEntity<Item> getItemById(@PathVariable("itemId") Long itemId){
		Item item=barterService.getItemById(itemId);
		return ResponseEntity.ok(item);
	}
}
package com.infra.meta.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clipmk.barter.service.BarterService;
import com.clipmk.board.service.BoardService;
import com.infra.meta.dto.ItemIdDto;
import com.infra.meta.dto.MetaResDto;
import com.infra.meta.dto.PostIdDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class MetaController {
	
	private final BoardService boardService;
	private final BarterService barterService;
	
	@GetMapping("/ids")
	public ResponseEntity<MetaResDto> postsIds(){
		
		List<PostIdDto> postsIds= boardService.getPostsIds();
		List<ItemIdDto> itemsIds= barterService.getItemsIds();
		
		MetaResDto metaResDto = MetaResDto
				.builder()
				.itemsIds(itemsIds)
				.postsIds(postsIds)
				.build(); 
		
		return ResponseEntity.ok(metaResDto);
	}
}

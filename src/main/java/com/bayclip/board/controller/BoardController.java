package com.bayclip.board.controller;

import org.springframework.beans.factory.annotation.Value;
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

import com.bayclip.board.dto.CommentRequestDto;
import com.bayclip.board.dto.PostDto;
import com.bayclip.board.dto.PostRequestDto;
import com.bayclip.board.dto.PostsResponseDto;
import com.bayclip.board.service.BoardService;
import com.bayclip.board.service.CommentService;
import com.bayclip.user.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;
	private final CommentService commentService;
	@Value("${application.post.best-post.viewCnt}")
	private Integer viewCount;
	
	//게시물 등록
	@PostMapping("/post")
	public ResponseEntity<Void> register(
			@RequestBody PostRequestDto request,
			@AuthenticationPrincipal User user
	){
		if(boardService.register(request, user.getId())) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	//게시물 조회 
	@GetMapping("/post/{post-id}")
	public ResponseEntity<PostDto> getPostById(
			@PathVariable("post-id") Long postId,
			@AuthenticationPrincipal User user){
		PostDto postDto= boardService.getPostById(postId, user);
	    return ResponseEntity.ok(postDto);
	}
	
	//게시물 페이징
	@GetMapping("/posts/{category}")
	public ResponseEntity<Page<PostsResponseDto>> posts(
			@PageableDefault(page=0, size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
			@PathVariable("category") String category,
			@RequestParam(value = "searchKeyword", defaultValue  = "") String searchKeyword){
		Page<PostsResponseDto> posts =null;
		if(searchKeyword.isEmpty()) {
			 posts = boardService.findAll(pageable, category).map(PostsResponseDto::from);
		}else {
			 posts = boardService.findByTitleContaining(pageable, searchKeyword).map(PostsResponseDto::from);
		}
		
		return ResponseEntity.ok(posts);
	}
	
	//베스트 게시물 
	@GetMapping("/best")
	public ResponseEntity<Page<PostsResponseDto>> bestPost(
			@PageableDefault(page=0, size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable){
		
		Page<PostsResponseDto> boards =null;
		boards = boardService.findByView_cntGreaterThan(viewCount, pageable).map(PostsResponseDto::from);
		return ResponseEntity.ok(boards);
	}

	//게시물 추천
	@PatchMapping("/post/{post-id}/recommend")
	public ResponseEntity<Boolean> recommendBoard(
			@PathVariable(value="post-id") Long postId,
			@RequestParam(value="value") int value,
			@AuthenticationPrincipal User user){
		
		return ResponseEntity.ok(boardService.recommendBoard(postId, user, value));
	}
	
	//댓글 달기
	@PostMapping("/{post-Id}/comment")
	public ResponseEntity<Void> addCommentToBoard(
			@PathVariable("postId") Long postId,
			@RequestBody CommentRequestDto request,
			@AuthenticationPrincipal User user){
		
		if(commentService.register(postId, user.getId(), request.getContent())) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	//댓글 삭제
	@DeleteMapping("/comment/delete/{commentId}")
	public ResponseEntity<Void> deleteComment(
			@PathVariable("commentId") Long commentId,
			@AuthenticationPrincipal User user){
		if(commentService.deleteComment(commentId, user)) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.noContent().build();
		}
	}
	
}
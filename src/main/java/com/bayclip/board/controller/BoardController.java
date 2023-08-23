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

import com.bayclip.board.dto.CommentCreateDto;
import com.bayclip.board.dto.EditCommentRequestDto;
import com.bayclip.board.dto.EditPostRequestDto;
import com.bayclip.board.dto.PostDto;
import com.bayclip.board.dto.PostRequestDto;
import com.bayclip.board.dto.PostsResponseDto;
import com.bayclip.board.dto.RecommendRequestDto;
import com.bayclip.board.service.BoardService;
import com.bayclip.board.service.CommentService;
import com.bayclip.user.entity.User;

import jakarta.servlet.http.HttpSession;
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
			HttpSession session,
//			@RequestParam(defaultValue = "50") int pageSize,
			@AuthenticationPrincipal User user
	){	
		
		PostDto postDto= boardService.getPostById(postId, user, session);
		
		
		if(postDto != null) {
			return ResponseEntity.ok(postDto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//게시물 수정
	@PatchMapping("/post/{post-id}")
	public ResponseEntity<Void> editPost(
			@PathVariable("post-id") Long postId,
			@RequestBody EditPostRequestDto request,
			@AuthenticationPrincipal User user
	){

		if(boardService.edit(postId, request, user)) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
		
	}
	
	//게시물 삭제
	@DeleteMapping("/post/{post-id}")
	public ResponseEntity<Void> deletePost(
			@PathVariable("post-id") Long postId,
			@AuthenticationPrincipal User user
	){	
		
		if(boardService.delete(postId, user)) {
			return ResponseEntity.ok().build();
		}
		else{
			return ResponseEntity.notFound().build();
		}
	}
	
	
	//게시물 페이징 and 검색
	@GetMapping("/posts/{category}")
	public ResponseEntity<Page<PostsResponseDto>> posts(
			@PageableDefault(page=0, size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
			@PathVariable("category") String category,
			@RequestParam(value = "searchTerm", defaultValue  = "") String searchTerm
			){
		
		Page<PostsResponseDto> posts =null;
		
		if(searchTerm.isEmpty()) {
			 posts = boardService.findAll(pageable, category).map(PostsResponseDto::from);
		}else {
			 posts = boardService.findByTitleContaining(pageable, searchTerm).map(PostsResponseDto::from);
		}
		
		return ResponseEntity.ok(posts);
	}
	
	//베스트 게시물 
	@GetMapping("/posts/best")
	public ResponseEntity<Page<PostsResponseDto>> bestPosts(
			@PageableDefault(page=0, size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable){
		
		Page<PostsResponseDto> boards =null;
		boards = boardService.findByViewCntGreaterThan(viewCount, pageable).map(PostsResponseDto::from);
		return ResponseEntity.ok(boards);
	}
	
	//게시물 추천
	@PatchMapping("/post/{post-id}/recommend")
	public ResponseEntity<Boolean> recommendBoard(
			@PathVariable(value="post-id") Long postId,
			@RequestBody RecommendRequestDto request,
			@AuthenticationPrincipal User user){
		if(boardService.recommend(postId, user, request.getValue())) {
			return ResponseEntity.ok().build();
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
	}
	
	//댓글 달기
	@PostMapping("/comment")
	public ResponseEntity<Void> createComment(
			@RequestBody CommentCreateDto request,
			@AuthenticationPrincipal User user){
		
		if(commentService.createComment(request.getPostId(), user, request.getContent())) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	//대댓글 달기
	@PostMapping("/comment/{parent-id}/reply")
	public ResponseEntity<Void> createReply(
			 @PathVariable(value="parent-id") Long parentId,
			 @RequestBody CommentCreateDto request,
			 @AuthenticationPrincipal User user
	){
		if(commentService.createReply(request.getPostId(), parentId, user, request.getContent())) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	//댓글 삭제
	@DeleteMapping("/comment/{commentId}")
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
	
	//댓글 수정
	@PatchMapping("/comment/{comment-id}")
	public ResponseEntity<Void> editPost(
			@PathVariable("comment-id") Long commentId,
			@RequestBody EditCommentRequestDto request,
			@AuthenticationPrincipal User user
	){
		if(commentService.edit(commentId, request, user)) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
		
	}
	
	//댓글 추천
	@PatchMapping("/comment/{comment-id}/recommend")
	public ResponseEntity<Boolean> recommendComment(
			@PathVariable(value="comment-id") Long commentId,
			@RequestBody RecommendRequestDto request,
			@AuthenticationPrincipal User user){
		if(commentService.recommend(commentId, user, request.getValue())) {
			return ResponseEntity.ok().build();
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}	
	}

}
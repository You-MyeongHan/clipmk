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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bayclip.board.entity.Board;
import com.bayclip.board.entity.BoardRequest;
import com.bayclip.board.entity.Comment;
import com.bayclip.board.entity.CommentRequest;
import com.bayclip.board.entity.PostResponse;
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
	
	@GetMapping("/{boardId}")
	public ResponseEntity<Board> getBoardById(@PathVariable("boardId") Long boardId){
		Board board= boardService.getBoardById(boardId);
	    return ResponseEntity.ok(board);
	}

	@PostMapping("/register")
	public ResponseEntity<Void> register(
			@RequestBody BoardRequest request,
			@AuthenticationPrincipal User user
	){
		if(boardService.register(request, user.getId())) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	@GetMapping("/post")
	public ResponseEntity<Page<PostResponse>> post(
			@PageableDefault(page=0, size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam(value = "category", defaultValue  = "humor") String group,
			@RequestParam(value = "searchKeyword", defaultValue  = "") String searchKeyword){
		Page<PostResponse> boards =null;
		if(searchKeyword==null) {
			 boards = boardService.findAll(pageable, group).map(PostResponse::from);
		}else {
			 boards = boardService.findByTitleContaining(pageable, searchKeyword).map(PostResponse::from);
		}
		
		return ResponseEntity.ok(boards);
	}
	
	@GetMapping("/best-post")
	public ResponseEntity<Page<PostResponse>> bestPost(
			@PageableDefault(page=0, size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable){
		
		Page<PostResponse> boards =null;
		boards = boardService.findByView_cntGreaterThan(viewCount, pageable).map(PostResponse::from);
		return ResponseEntity.ok(boards);
	}


	@GetMapping("/recommendBoard")
	public ResponseEntity<Boolean> recommendBoard(
			@RequestParam(value="boardId") Long boardId,
			@AuthenticationPrincipal User user){
		
		return ResponseEntity.ok(boardService.recommendBoard(boardId, user.getId()));
	}
	
	@PostMapping("/{boardId}/comment")
	public ResponseEntity<Void> addCommentToBoard(
			@PathVariable("boardId") Long boardId,
			@RequestBody CommentRequest request,
			@AuthenticationPrincipal User user){
		
		if(commentService.register(boardId, user.getId(), request.getContent())) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	@DeleteMapping("/{boardId}/comment/delete/{commentId}")
	public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId){
		commentService.deleteComment(commentId);
		return ResponseEntity.noContent().build();
	}
	
}
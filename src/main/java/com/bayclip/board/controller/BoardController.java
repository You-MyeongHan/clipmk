package com.bayclip.board.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bayclip.board.dto.CommentCreateDto;
import com.bayclip.board.dto.CommentDto;
import com.bayclip.board.dto.EditCommentReqDto;
import com.bayclip.board.dto.EditPostReqDto;
import com.bayclip.board.dto.FileResDto;
import com.bayclip.board.dto.PostDto;
import com.bayclip.board.dto.PostReqDto;
import com.bayclip.board.dto.PostsResDto;
import com.bayclip.board.dto.RecommendCntResDto;
import com.bayclip.board.dto.RecommendReqDto;
import com.bayclip.board.service.BoardService;
import com.bayclip.board.service.CommentService;
import com.bayclip.board.service.FileUploadService;
import com.bayclip.user.entity.User;

import jakarta.servlet.http.HttpServletResponse;
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
	private final FileUploadService fileUploadService;
	
	//s3 이미지 등록
    @PostMapping("/s3/upload")
    public ResponseEntity<FileResDto> fileUploadFromCKEditor(
    		HttpServletResponse response,
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {

        return new ResponseEntity<>(FileResDto.builder().
                uploaded(true).
                url(fileUploadService.upload(image)).
                build(), HttpStatus.OK);
    }
    
	//게시물 등록
	@PostMapping("/post")
	public ResponseEntity<Void> register(
			@RequestBody PostReqDto request,
			@AuthenticationPrincipal User user
	){
		
		boardService.register(request, user.getId());
		
		return ResponseEntity.ok().build();
	}
	
	//게시물 조회 
	@GetMapping("/post/{post-id}")
	public ResponseEntity<PostDto> getPostById(
			@PathVariable("post-id") Long postId,
			HttpSession session,
			@AuthenticationPrincipal User user
	){	
		
		PostDto postDto= boardService.getPostById(postId, user, session);
		
		return ResponseEntity.ok(postDto);
	}
	
	//게시물 수정
	@PatchMapping("/post/{post-id}")
	public ResponseEntity<Void> editPost(
			@PathVariable("post-id") Long postId,
			@RequestBody EditPostReqDto request,
			@AuthenticationPrincipal User user
	){

		boardService.edit(postId, request, user);
		return ResponseEntity.ok().build();
		
	}
	
	//게시물 삭제
	@DeleteMapping("/post/{post-id}")
	public ResponseEntity<Void> deletePost(
			@PathVariable("post-id") Long postId,
			@AuthenticationPrincipal User user
	){	
		
		boardService.delete(postId, user);
		return ResponseEntity.ok().build();
	}
	
	
	//게시물 페이징 and 검색
	@GetMapping("/posts/{table}")
	public ResponseEntity<Page<PostsResDto>> posts(
			@PageableDefault(page=0, size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
			@PathVariable("table") String table,
			@RequestParam(value="group", defaultValue  = "") String group,
			@RequestParam(value = "searchTerm", defaultValue  = "") String searchTerm
			){
		
		Page<PostsResDto> posts =null;
		
		if(searchTerm.isEmpty()) {
			 posts = boardService.findAll(pageable, table, group).map(PostsResDto::from);
		}else {
			 posts = boardService.findByTitleContaining(pageable, searchTerm).map(PostsResDto::from);
		}
		
		return ResponseEntity.ok(posts);
	}
	
	//베스트 게시물 
	@GetMapping("/posts/best")
	public ResponseEntity<Page<PostsResDto>> bestPosts(
			@PageableDefault(page=0, size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable){
		
		Page<PostsResDto> boards =null;
		boards = boardService.findByViewCntGreaterThan(viewCount, pageable).map(PostsResDto::from);
		return ResponseEntity.ok(boards);
	}
	
	//게시물 추천
	@PatchMapping("/post/{post-id}/recommend")
	public ResponseEntity<Boolean> recommendBoard(
			@PathVariable(value="post-id") Long postId,
			@RequestBody RecommendReqDto request,
			@AuthenticationPrincipal User user){
		
		boardService.recommend(postId, user, request.getValue());
		return ResponseEntity.ok().build();
	}
	
	//게시물 추천 수 조회
	@GetMapping("/post/{post-id}/recommend")
	public ResponseEntity<RecommendCntResDto> recommendBoard(
			@PathVariable(value="post-id") Long postId,
			@AuthenticationPrincipal User user){
		
		RecommendCntResDto recommendCntResponseDto=commentService.getRecommendCnt(postId,user);
		
		if(recommendCntResponseDto!= null) {
			return ResponseEntity.ok(recommendCntResponseDto);
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	//댓글 조회
	@GetMapping("/comment/{post-id}")
	public ResponseEntity<List<CommentDto>> getComments(
			@PathVariable(value="post-id") Long postId,
			@AuthenticationPrincipal User user){
		List<CommentDto> commentDtos= commentService.getComments(postId);
		if(commentDtos != null) {
			return ResponseEntity.ok(commentDtos);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//댓글 달기
	@PostMapping("/comment")
	public ResponseEntity<Void> createComment(
			@RequestBody CommentCreateDto request,
			@AuthenticationPrincipal User user){
		
		commentService.createComment(request.getPostId(), user, request.getContent());
		
		return ResponseEntity.ok().build();
	}
	
	//대댓글 달기
	@PostMapping("/comment/{parent-id}/reply")
	public ResponseEntity<Void> createReply(
			 @PathVariable(value="parent-id") Long parentId,
			 @RequestBody CommentCreateDto request,
			 @AuthenticationPrincipal User user
	){
		
		commentService.createReply(request.getPostId(), parentId, user, request.getContent());
		
		return ResponseEntity.ok().build();
	}
	
	//댓글 삭제
	@DeleteMapping("/comment/{commentId}")
	public ResponseEntity<Void> deleteComment(
			@PathVariable("commentId") Long commentId,
			@AuthenticationPrincipal User user){
		commentService.deleteComment(commentId, user);
		return ResponseEntity.ok().build();
	}
	
	//댓글 수정
	@PatchMapping("/comment/{comment-id}")
	public ResponseEntity<Void> editPost(
			@PathVariable("comment-id") Long commentId,
			@RequestBody EditCommentReqDto request,
			@AuthenticationPrincipal User user
	){
		commentService.edit(commentId, request, user);
		return ResponseEntity.ok().build();
		
	}
	
	//댓글 추천
	@PatchMapping("/comment/{comment-id}/recommend")
	public ResponseEntity<Boolean> recommendComment(
			@PathVariable(value="comment-id") Long commentId,
			@RequestBody RecommendReqDto request,
			@AuthenticationPrincipal User user){
		commentService.recommend(commentId, user, request.getValue());
		return ResponseEntity.ok().build();

	}

}
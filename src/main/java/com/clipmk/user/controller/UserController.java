package com.clipmk.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clipmk.auth.dto.NickReqDto;
import com.clipmk.auth.dto.RegisterReqDto;
import com.clipmk.auth.dto.UidReqDto;
import com.clipmk.barter.dto.ItemsResDto;
import com.clipmk.barter.service.BarterService;
import com.clipmk.board.dto.CommentsResDto;
import com.clipmk.board.dto.PostsResDto;
import com.clipmk.board.service.BoardService;
import com.clipmk.board.service.CommentService;
import com.clipmk.user.dto.EditUserReqDto;
import com.clipmk.user.dto.UserInfoDto;
import com.clipmk.user.dto.UserResDto;
import com.clipmk.user.entity.User;
import com.clipmk.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	private final BoardService boardService;
	private final CommentService commentService;
	private final UserService userService;
	private final BarterService barterService;
	
	//회원 정보 조회
	@GetMapping
	public ResponseEntity<UserResDto> getUserInfo(
			@AuthenticationPrincipal User user){
		
		PageRequest pageable = PageRequest.of(0, 10);
		Page<PostsResDto> postsResDto=boardService.findByUserId(user.getId(), pageable).map(PostsResDto::from);
		Page<CommentsResDto> commentsResDto=commentService.findByUserId(user.getId(), pageable).map(CommentsResDto::from);
		Page<ItemsResDto> itemsResDto=barterService.findByUserId(user.getId(), pageable).map(ItemsResDto::from);
		
		UserInfoDto userInfoDto= UserInfoDto.builder()
				.id(user.getId())
				.uid(user.getUid())
				.nick(user.getNick())
				.email(user.getEmail())
				.emailReceive(user.getEmailReceive())
				.build();
		
		UserResDto userInfo=UserResDto.builder()
				.user(userInfoDto)
				.posts(postsResDto)
				.comments(commentsResDto)
				.items(itemsResDto)
				.build();
		
		logger.info("User information retrieved successfully: {}", userInfo);
		return ResponseEntity.ok(userInfo);
	}
	
	//회원 가입
	@PostMapping
	public ResponseEntity<Integer> register(
			@RequestBody RegisterReqDto request
	){
		
		int user_id = userService.register(request);
		if(user_id > 0) {
			logger.info("User registered successfully: {}", request.getUid());;
			return ResponseEntity.ok(user_id);
		}
		else {
			logger.warn("User registration failed for uid: {}", request.getUid());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	//회원 정보 수정
	//수정예정
	@PatchMapping
	public ResponseEntity<Void> update(
			@RequestBody EditUserReqDto request,
			@AuthenticationPrincipal User user
	){
		userService.edit(request, user);
		return ResponseEntity.ok().build();
	}
	
	//회원 탈퇴
	//수정예정
	@DeleteMapping
	public ResponseEntity<Void> delete(){
		return ResponseEntity.ok().build();
	}

	//uid 중복검사
	@PostMapping("/check-uid")
	public ResponseEntity<Void> checkUidDuplication(
			@RequestBody UidReqDto request
	){
		if(userService.existsByUid(request.getUid())) {
			logger.info("UID {} is not duplicated.", request.getUid());
			return ResponseEntity.ok().build();
		}
		else {
			logger.info("UID {} is duplicated.", request.getUid());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
	}
	
	//닉네임 중복검사
	@PostMapping("/check-nick")
	public ResponseEntity<?> checkNickDuplication(
			@RequestBody NickReqDto request){
		if(userService.existsByNick(request.getNick())) {
			logger.info("Nick {} is not duplicated.", request.getNick());
			return ResponseEntity.ok().build();
		}
		else {
			logger.info("Nick {} is duplicated.", request.getNick());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
	}
	
	//유저 아이템 페이징
	@GetMapping("/items")
	public ResponseEntity<Page<ItemsResDto>> items(
			@PageableDefault(page=0, size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
			@AuthenticationPrincipal User user){
		
		 Page<ItemsResDto> itemsResDto=barterService.findByUserId(user.getId(), pageable).map(ItemsResDto::from);
		
		return ResponseEntity.ok(itemsResDto);
	}
	
	//유저 게시물 페이징
	@GetMapping("/posts")
	public ResponseEntity<Page<PostsResDto>> posts(
			@PageableDefault(page=0, size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
			@AuthenticationPrincipal User user){
		
		 Page<PostsResDto> postsResDto=boardService.findByUserId(user.getId(), pageable).map(PostsResDto::from);
		
		return ResponseEntity.ok(postsResDto);
	}
	
	//유저 댓글 페이징
	@GetMapping("/comments")
	public ResponseEntity<Page<CommentsResDto>> comments(
			@PageableDefault(page=0, size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
			@AuthenticationPrincipal User user){
		
		 Page<CommentsResDto> commentsResDto=commentService.findByUserId(user.getId(), pageable).map(CommentsResDto::from);
		
		return ResponseEntity.ok(commentsResDto);
	}
	
	//찜한 아이템 페이징
	@GetMapping("/dibs")
	public ResponseEntity<Page<ItemsResDto>> dibs(
			@PageableDefault(page=0, size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
			@AuthenticationPrincipal User user){
		
		Page<ItemsResDto> itemsResDto=barterService.findDibByUserId(user.getId(), pageable).map(ItemsResDto::from);
		
		return ResponseEntity.ok(itemsResDto);
	}
}
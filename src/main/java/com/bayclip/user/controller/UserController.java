package com.bayclip.user.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bayclip.auth.dto.NickRequestDto;
import com.bayclip.auth.dto.RegisterRequestDto;
import com.bayclip.auth.dto.UidRequestDto;
import com.bayclip.board.service.BoardService;
import com.bayclip.board.service.CommentService;
import com.bayclip.user.dto.EditUserRequestDto;
import com.bayclip.user.dto.UserInfoDto;
import com.bayclip.user.entity.User;
import com.bayclip.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
	
	private final BoardService boardService;
	private final CommentService commentService;
	private final UserService userService;
	
	//회원 정보 조회
	@GetMapping("")
	public ResponseEntity<UserInfoDto> getUserInfo(
			@RequestParam(value="act", defaultValue="userInfo") String act,
			@AuthenticationPrincipal User user){
		
		PageRequest pageable = PageRequest.of(0, 10);
		
		if(act.equals("userInfo")) {
			UserInfoDto userInfo=UserInfoDto.builder()
					.id(user.getId())
					.nick(user.getNick())
					.uid(user.getUid())
					.email(user.getEmail())
					.emailReceive(user.getEmailReceive())
					.posts(boardService.findTop10ByUser_IdOrderByIdDesc(user.getId(), pageable))
					.comments(commentService.findTop10ByUser_IdOrderByComment_IdDesc(user.getId(), pageable))
					.build();
			return ResponseEntity.ok(userInfo);
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	//회원 가입
	@PostMapping("")
	public ResponseEntity<Void> register(
			@RequestBody RegisterRequestDto request
	){
		if(userService.register(request)) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	//회원 정보 수정
	//수정예정
	@PatchMapping("")
	public ResponseEntity<Void> update(
			@RequestBody EditUserRequestDto request,
			@AuthenticationPrincipal User user
	){
		if(userService.edit(request, user)) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	//회원 탈퇴
	//수정예정
	@DeleteMapping("")
	public ResponseEntity<Void> delete(){
		return ResponseEntity.ok().build();
	}

	//uid 중복검사
	@PostMapping("/check-uid")
	public ResponseEntity<Void> checkUidDuplication(
			@RequestBody UidRequestDto request
	){
		if(userService.existsByUid(request.getUid())) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
	}
	
	//email 중복검사
	@PostMapping("/check-nick")
	public ResponseEntity<?> checkEmailDuplication(
			@RequestBody NickRequestDto request){
		if(userService.existsByNick(request.getNick())) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
	}
	
//	@GetMapping("/boards")
//	public ResponseEntity<Page<PostResponse>> boards(
//			@PageableDefault(page=0, size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable){
//		
//		return ResponseEntity.ok(boardService.findByUser_Id(pageable, ).map(PostResponse::from));
//		
//	}
}

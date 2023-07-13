package com.bayclip.security.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bayclip.security.auth.entity.AuthenticationRequest;
import com.bayclip.security.auth.entity.AuthenticationResponse;
import com.bayclip.security.auth.entity.EmailRequest;
import com.bayclip.security.auth.entity.LoginResponse;
import com.bayclip.security.auth.entity.RegisterRequest;
import com.bayclip.security.auth.entity.UidRequest;
import com.bayclip.security.auth.service.AuthenticationService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	
	private final AuthenticationService userService;
	
	@PostMapping("/register")
	public ResponseEntity<Void> register(
			@RequestBody RegisterRequest request
	){
		if(userService.register(request)) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<LoginResponse> authenticate(
			@RequestBody AuthenticationRequest request,
			HttpServletResponse response
	){
		AuthenticationResponse authenticationResponse = userService.authenticate(request);

        response.setHeader("Authorization", "Bearer " + authenticationResponse.getAccessToken());

        Cookie refreshTokenCookie = new Cookie("refreshToken", authenticationResponse.getRefreshToken());
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); //7 days
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
//        refreshTokenCookie.setSecure(true); // HTTPS에서만 전송되도록 설정 (필요에 따라 변경)
        response.addCookie(refreshTokenCookie);

        LoginResponse loginResponse = new LoginResponse(authenticationResponse.getNick(), authenticationResponse.getEmail());
        
        return ResponseEntity.ok(loginResponse);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(
			HttpServletRequest request,
		    HttpServletResponse response
	){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication != null) {
	        // 로그아웃 처리
	        SecurityContextHolder.clearContext();

	        // Access Token을 헤더에서 삭제
	        response.setHeader("Authorization", "");

	        // Refresh Token을 쿠키에서 삭제
	        Cookie refreshTokenCookie = new Cookie("refreshToken", "");
	        refreshTokenCookie.setMaxAge(0);
	        refreshTokenCookie.setPath("/");
	        refreshTokenCookie.setHttpOnly(true);
	        refreshTokenCookie.setSecure(true); // HTTPS에서만 전송되도록 설정 (필요에 따라 변경)
	        response.addCookie(refreshTokenCookie);
	    }
		
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/checkUid")
	public ResponseEntity<Void> checkUidDuplication(
			@RequestBody UidRequest request){
		if(userService.existsByUid(request.getUid())) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
	}
	
	@PostMapping("/checkEmail")
	public ResponseEntity<?> checkEmailDuplication(
			@RequestBody EmailRequest request){
		if(userService.existsByEmail(request.getEmail())) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
	}
	
//	@PostMapping("/refresh-token")
//	  public void refreshToken(
//	      HttpServletRequest request,
//	      HttpServletResponse response
//	  ) throws IOException {
//		userService.refreshToken(request, response);
//	  }
}
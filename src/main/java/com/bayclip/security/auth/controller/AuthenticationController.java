package com.bayclip.security.auth.controller;

import java.io.IOException;

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
import com.bayclip.security.auth.entity.RegisterRequest;
import com.bayclip.security.auth.entity.UidRequest;
import com.bayclip.security.auth.service.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	
	private final AuthenticationService userService;
	
	@PostMapping("/register")
	public ResponseEntity<Boolean> register(
			@RequestBody RegisterRequest request
	){
		return ResponseEntity.ok(userService.register(request));
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(
			@RequestBody AuthenticationRequest request
	){
		return ResponseEntity.ok(userService.authenticate(request));
	}
	
	@PostMapping("/logout")
	public void logout(
			HttpServletRequest request,
		    HttpServletResponse response
	){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		userService.logout(request, response, auth);
	}
	
	@PostMapping("/checkUid")
	public ResponseEntity<?> checkUidDuplication(
			@RequestBody UidRequest request){
		return ResponseEntity.ok(userService.existsByUid(request.getUid()));
	}
	
	@PostMapping("/checkEmail")
	public ResponseEntity<?> checkEmailDuplication(
			@RequestBody EmailRequest request){
		return ResponseEntity.ok(userService.existsByEmail(request.getEmail()));
	}
	
	@PostMapping("/refresh-token")
	  public void refreshToken(
	      HttpServletRequest request,
	      HttpServletResponse response
	  ) throws IOException {
		userService.refreshToken(request, response);
	  }
}
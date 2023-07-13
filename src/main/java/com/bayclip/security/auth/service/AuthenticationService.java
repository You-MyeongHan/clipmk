package com.bayclip.security.auth.service;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bayclip.security.auth.entity.AuthenticationRequest;
import com.bayclip.security.auth.entity.AuthenticationResponse;
import com.bayclip.security.auth.entity.RegisterRequest;
import com.bayclip.security.token.entity.Token;
import com.bayclip.security.token.entity.TokenType;
import com.bayclip.security.token.service.JwtService;
import com.bayclip.user.entity.Role;
import com.bayclip.user.entity.User;
import com.bayclip.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements LogoutHandler{
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	
	public boolean register(RegisterRequest request) {
		
		var user=User.builder()
				.uid(request.getUid())
				.pwd(passwordEncoder.encode(request.getPwd()))
				.nick(request.getNick())
				.email(request.getEmail())
				.emailReceive(request.getEmailReceive())
				.role(Role.USER)
				.build();
		userRepository.save(user);
		
		return true;
	}

	public AuthenticationResponse authenticate(
			AuthenticationRequest request) {
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
					request.getUid(),
					request.getPwd()
			)
		);	
		var user=userRepository.findByUid(request.getUid())
				.orElseThrow();
		var accessToken=jwtService.generateAccessToken(user.getId().toString());
		var refreshToken = jwtService.generateRefreshToken(user.getId().toString());
		
		return AuthenticationResponse.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.nick(user.getNick())
				.email(user.getEmail())
				.build();
	}
	
	//수정 필요
	@Override
	public void logout(
			HttpServletRequest request,
		    HttpServletResponse response,
		    Authentication authentication
	){
		final String authHeader = request.getHeader("Authorization");
	    
	    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
	      return;
	    }
	    SecurityContextHolder.clearContext();
	}
	
	public boolean existsByUid(String uid){
		return !userRepository.existsByUid(uid);
	}
	
	public boolean existsByEmail(String email){
		return !userRepository.existsByEmail(email);
	}
	
//	public void refreshToken(
//			HttpServletRequest request,
//		    HttpServletResponse response	
//	) throws IOException {
//		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//	    final String refreshToken;
//	    final String id;
//	    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
//	      return;
//	    }
//	    
//	    refreshToken = authHeader.substring(7);
//	    id = jwtService.extractId(refreshToken);
//	    if(id!=null) {
//	    	var user= this.userRepository.findById(Integer.parseInt(id)).orElseThrow();
//	    	
//	    	if(jwtService.isTokenValid(refreshToken, user)) {
//	    		var accessToken=jwtService.generateAccessToken(user);
//	            var authResponse = AuthenticationResponse.builder()
//	                    .accessToken(accessToken)
//	                    .refreshToken(refreshToken)
//	                    .build();
//	            
//	            response.setStatus(HttpServletResponse.SC_OK);
//	            response.setContentType("application/json");
//	            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//	            
//	    	}
//	    }
//	}
	
}

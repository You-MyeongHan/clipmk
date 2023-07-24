package com.bayclip.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.bayclip.auth.dto.LoginRequestDto;
import com.bayclip.auth.dto.AuthResponseDto;
import com.bayclip.config.TokenProvider;
import com.bayclip.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements LogoutHandler{
	
	private final UserRepository userRepository;
	private final TokenProvider tokenProvider;
	private final AuthenticationManager authenticationManager;

	public AuthResponseDto authenticate(
			LoginRequestDto request) {
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
					request.getUid(),
					request.getPwd()
			)
		);	
		var user=userRepository.findByUid(request.getUid())
				.orElseThrow();
		var accessToken=tokenProvider.generateAccessToken(user.getId().toString());
		var refreshToken = tokenProvider.generateRefreshToken(user.getId().toString());
		
		return AuthResponseDto.builder()
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

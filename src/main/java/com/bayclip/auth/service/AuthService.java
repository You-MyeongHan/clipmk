package com.bayclip.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.bayclip.auth.dto.AuthResponseDto;
import com.bayclip.auth.dto.LoginRequestDto;
import com.bayclip.user.repository.UserRepository;
import com.global.config.TokenProvider;
import com.global.error.errorCode.AuthErrorCode;
import com.global.error.exception.RestApiException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements LogoutHandler{
	
	private final UserRepository userRepository;
	private final TokenProvider tokenProvider;
	private final AuthenticationManager authenticationManager;

	public AuthResponseDto authenticate(LoginRequestDto request) {
		var user=userRepository.findByUid(request.getUid())
				.orElseThrow(()-> new RestApiException(AuthErrorCode.INVALID_CREDENTIALS));
		
		try {
		    authenticationManager.authenticate(
		        new UsernamePasswordAuthenticationToken(
		            request.getUid(),
		            request.getPwd()
		        )
		    );
		} catch (BadCredentialsException e) {
		    throw new RestApiException(AuthErrorCode.INVALID_CREDENTIALS);
		}
		
		var accessToken=tokenProvider.generateAccessToken(user.getId().toString(), user.getNick());
		var refreshToken = tokenProvider.generateRefreshToken(user.getId().toString());
		
		return AuthResponseDto.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.id(user.getId())
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
	
//	public String renew-accessToken
	
}

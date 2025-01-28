package com.clipmk.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clipmk.auth.dto.AuthResDto;
import com.clipmk.auth.dto.LoginReqDto;
import com.clipmk.auth.dto.LoginResDto;
import com.clipmk.auth.service.AuthService;
import com.global.config.TokenProvider;
import com.global.error.errorCode.AuthErrorCode;
import com.global.error.exception.RestApiException;
import com.infra.email.dto.EmailRequestDto;
import com.infra.email.dto.EmailVerifyRequestDto;
import com.infra.email.service.MailService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final MailService mailService;
	private final TokenProvider tokenProvider;
	
	//로그인
	@PostMapping("/login")
	public ResponseEntity<LoginResDto> authenticate(
			@RequestBody LoginReqDto request,
			HttpServletResponse response
	){
		AuthResDto authResponse = authService.authenticate(request);

        response.setHeader("Authorization", "Bearer " + authResponse.getAccessToken());

        Cookie refreshTokenCookie = new Cookie("refreshToken", authResponse.getRefreshToken());
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); //7 days
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setDomain("clipmk.com");
        refreshTokenCookie.setHttpOnly(true);
//        refreshTokenCookie.setSecure(true); // HTTPS에서만 전송되도록 설정 (필요에 따라 변경)
        response.addCookie(refreshTokenCookie);
        response.setHeader("access-control-expose-headers", "Authorization");
//        response.setHeader("Set-Cookie", "SameSite=None; Secure");
        LoginResDto loginResponse = new LoginResDto(authResponse.getId(), authResponse.getNick(), authResponse.getEmail());
        
        return ResponseEntity.ok(loginResponse);
	}
	
	//로그아웃
	@GetMapping("/logout")
	public ResponseEntity<Void> logout(
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
	        refreshTokenCookie.setDomain("clipmk.com");
	        refreshTokenCookie.setHttpOnly(true);
	        refreshTokenCookie.setSecure(true); // HTTPS에서만 전송되도록 설정 (필요에 따라 변경)
	        response.addCookie(refreshTokenCookie);
	        
	        return ResponseEntity.ok().build();
	    }else {
	    	throw new RestApiException(AuthErrorCode.INVALID_TOKEN);
	    }
		
	}
	
	@PostMapping("/email-authcode")
	public ResponseEntity<Void> sendEmailVerification(@RequestBody EmailRequestDto request){
		
		mailService.sendVerificationEmail(request.getEmail());
		
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/verify-authcode")
	public ResponseEntity<Void> verifyEmail(@RequestBody EmailVerifyRequestDto request){
		
		String email = request.getEmail();
        String authCode = request.getAuthCode();
		
		if (mailService.isValidAuthCode(email, authCode)) {
			return ResponseEntity.ok().build();
		}else {
			throw new RestApiException(AuthErrorCode.INVALID_AUTHCODE);
		}	
	}
	
	@PostMapping("/renew-token")
	public ResponseEntity<Void> refreshToken(
	    HttpServletRequest request,
	    HttpServletResponse response
	) {
		String accessToken = tokenProvider.renewAccessToken();
		if (accessToken != null) {
			response.setHeader("Authorization", "Bearer " + accessToken);
			return ResponseEntity.ok().build();
		}else {
			return ResponseEntity.badRequest().build();
		}	
	}
}
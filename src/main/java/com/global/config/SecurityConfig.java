package com.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;
	private final CorsConfig corsConfig;
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll() // 모든 경로에 대해 접근 허용
            )
            .csrf(csrf -> csrf.disable()); // CSRF 보호 비활성화 (개발 환경용)

        return http.build();
    }
// 	@Bean
// 	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
// 		http
// 			// CSRF 설정
// 			.csrf(AbstractHttpConfigurer::disable)
// 			// 권한 설정
// 			.authorizeHttpRequests((authorizeRequests) ->
//             	authorizeRequests
//             	.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
            	
//             	// user 요청
//             	.requestMatchers(HttpMethod.GET, "/user").authenticated() 	//회원 정보 조회
//             	.requestMatchers(HttpMethod.POST, "/user").permitAll()	  	//회원 가입
//             	.requestMatchers(HttpMethod.PATCH, "/user").authenticated()	//회원 정보 수정
//             	.requestMatchers(HttpMethod.DELETE, "/user").authenticated()//회원 탈퇴
//             	.requestMatchers(HttpMethod.POST, "/user/check-uid").permitAll()	//uid 중복검사
//             	.requestMatchers(HttpMethod.POST, "/user/check-nick").permitAll()	//nick 중복검사
//             	.requestMatchers(HttpMethod.GET, "/user/items").authenticated()	//유저 아이템 페이징
//             	.requestMatchers(HttpMethod.GET, "/user/posts").authenticated()	//유저 게시글 페이징
//             	.requestMatchers(HttpMethod.GET, "/user/comments").authenticated()	//유저 댓글 페이징
//             	.requestMatchers(HttpMethod.GET, "/user/dibs").authenticated()	//찜한 아이템 페이징
            	
//             	// auth 요청
//             	.requestMatchers(HttpMethod.POST, "/auth/login").permitAll()	//로그인
//             	.requestMatchers(HttpMethod.GET, "/auth/logout").authenticated()//로그아웃
// //            	.requestMatchers(HttpMethod.POST, "/auth/auto-logout").authenticated()	//자동 로그아웃
//             	.requestMatchers(HttpMethod.POST, "/auth/email-authcode").permitAll()	//email 인증코드 생성
//             	.requestMatchers(HttpMethod.POST, "/auth/verify-authcode").permitAll()	//email 인증코드 확인
//             	.requestMatchers(HttpMethod.POST, "/auth/renew-token").permitAll()	//토큰 재발급
            	
//             	// board 요청
//             	.requestMatchers(HttpMethod.POST, "/board/post").permitAll()		//게시물 등록
//             	.requestMatchers(HttpMethod.GET, "/board/post/{post-Id}").permitAll()	//게시물 조회
//             	.requestMatchers(HttpMethod.PATCH, "/board/post/{post-Id}").authenticated()	//게시물 수정
//             	.requestMatchers(HttpMethod.DELETE, "/board/post/{post-Id}").authenticated()	//게시물 삭제
//             	.requestMatchers(HttpMethod.GET, "/board/posts/{category}").permitAll()			//게시물 페이징
//             	.requestMatchers(HttpMethod.GET, "/board/posts/best").permitAll()		//베스트 게시물 
//             	.requestMatchers(HttpMethod.PATCH, "/board/post/{post-id}/recommend").authenticated()//게시물 추천
//             	.requestMatchers(HttpMethod.GET, "/board/post/{post-id}/recommend").permitAll()//게시물 추천
//             	.requestMatchers(HttpMethod.GET, "/board/comment/{post-id}").permitAll()	//댓글 조회
//             	.requestMatchers(HttpMethod.POST, "/board/comment").authenticated()	//댓글 달기
//             	.requestMatchers(HttpMethod.POST, "/board/comment/{parent-id}/reply").authenticated()	//대댓글 달기
//             	.requestMatchers(HttpMethod.DELETE, "/board/comment/{commentId}").authenticated()//댓글 삭제
//             	.requestMatchers(HttpMethod.PATCH, "/board/comment/{comment-id}").authenticated()//댓글 수정
//             	.requestMatchers(HttpMethod.PATCH, "/board/comment/{comment-id}/recommend").authenticated()//댓글 추천
//             	.requestMatchers(HttpMethod.POST, "/board/s3/upload").authenticated()//미디어 리소스 등록
            	
            	
//             	//barter 요청
//             	.requestMatchers(HttpMethod.POST, "/barter/item").authenticated()		//아이템 등록
//             	.requestMatchers(HttpMethod.GET, "/barter/item/{item-id}").permitAll()		//아이템 조회
//             	.requestMatchers(HttpMethod.PATCH, "/barter/item/{item-id}").authenticated()		//아이템 수정
//             	.requestMatchers(HttpMethod.DELETE, "/barter/item/{item-id}").authenticated()		//아이템 삭제
//             	.requestMatchers(HttpMethod.GET, "/barter/items").permitAll()		//아이템 페이징
//             	.requestMatchers(HttpMethod.POST, "/barter/item/{from-item-id}/suggest/{to-item-id}").authenticated()		//거래 제안
//             	.requestMatchers(HttpMethod.POST, "/barter/accept").authenticated()			//거래 수락
//             	.requestMatchers(HttpMethod.PATCH, "/barter/{item-id}/dib").authenticated()	//아이템 찜
            	
//             	//메타 데이터
//             	.requestMatchers(HttpMethod.GET, "/ids").permitAll()//미디어 리소스 등록
            	
//             	//test
//             	.requestMatchers(HttpMethod.GET, "/test").permitAll()
            	
// //            	chat 요청
// //            	.requestMatchers(HttpMethod.GET, "/chat/rooms").authenticated()			//채팅방 페이징
// //            	.requestMatchers(HttpMethod.GET, "/chat/history").authenticated()		//채팅방 조회
// //            	.requestMatchers(HttpMethod.POST, "/chat/send-message").authenticated()			//메세지 보내기
// //            	.requestMatchers(HttpMethod.POST, "/chat/room").permitAll()	//채팅방 생성
//             	.anyRequest().permitAll())
// //            	.anyRequest().hasAnyRole("ADMIN"))
			
// 			.sessionManagement((sessionManagement) ->
//             	sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
// 			.addFilter(corsConfig.corsFilter())
// 			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
// 			.authenticationProvider(authenticationProvider);
		
// 		return http.build();
// 	}
}
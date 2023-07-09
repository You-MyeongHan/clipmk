package com.bayclip.security.config;

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
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
			// CSRF 설정
			.csrf(AbstractHttpConfigurer::disable)
			// 권한 설정
			.authorizeHttpRequests((authorizeRequests) ->
            	authorizeRequests
            	.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
            	
            	//auth 요청
            	.requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
            	.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll()
            	.requestMatchers(HttpMethod.POST, "/auth/logout").authenticated()
            	.requestMatchers(HttpMethod.GET, "/auth/checkUid").permitAll()
            	.requestMatchers(HttpMethod.GET, "/auth/checkEmail").permitAll()
            	.requestMatchers(HttpMethod.GET, "/auth/refresh-token").authenticated()
            	
            	//board 요청
            	.requestMatchers(HttpMethod.GET, "/board/{boardId}").permitAll()
            	.requestMatchers(HttpMethod.POST, "/board/register").authenticated()
            	.requestMatchers(HttpMethod.GET, "/board/post").permitAll()
            	.requestMatchers(HttpMethod.GET, "/board/best-post").permitAll()
            	.requestMatchers(HttpMethod.GET, "/board/recommendBoard").authenticated()
            	.requestMatchers(HttpMethod.POST, "/board/{boardId}/comment").authenticated()
            	.requestMatchers(HttpMethod.DELETE, "/board/{boardId}/comment/delete/{commentId}").authenticated()
            	.anyRequest().hasAnyRole("ADMIN"))		
			
			.sessionManagement((sessionManagement) ->
            	sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilter(corsConfig.corsFilter())
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
			.authenticationProvider(authenticationProvider);
		
		return http.build();
	}
}
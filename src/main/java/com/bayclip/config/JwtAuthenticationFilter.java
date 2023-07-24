package com.bayclip.config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bayclip.user.entity.User;
import com.bayclip.user.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private final TokenProvider tokenProvider;
	private final UserService userService;
	
	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain)throws ServletException, IOException{
		
		final String authHeader=request.getHeader("Authorization");
		final String jwt;
		final String id;
		
		if(authHeader==null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		jwt=authHeader.substring(7);
		id=tokenProvider.extractId(jwt);
		
		if(id!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			User user=userService.loadUserByUsername(id);
			if(tokenProvider.isTokenValid(jwt,user)) {
				UsernamePasswordAuthenticationToken authToken =new UsernamePasswordAuthenticationToken (
					user,
					null,
					user.getAuthorities()
				);
				authToken.setDetails(
					new WebAuthenticationDetailsSource().buildDetails(request)
				);
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request,response);
	}
}

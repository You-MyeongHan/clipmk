package com.bayclip.user.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bayclip.auth.dto.RegisterRequestDto;
import com.bayclip.user.dto.EditUserRequestDto;
import com.bayclip.user.entity.Role;
import com.bayclip.user.entity.User;
import com.bayclip.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{
	
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public boolean register(RegisterRequestDto request) {
		
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
	
	public boolean edit(EditUserRequestDto request, User user) {
		
		if(user != null) {
			String newNick=request.getNick();
			
			if(newNick != null) {
				user.setNick(newNick);
			}
			userRepository.save(user);
			return true;
		}
		return false;
	}
	
	@Override
	public User loadUserByUsername(String id) throws UsernameNotFoundException{
		User user=userRepository.findById(Integer.parseInt(id)).orElseThrow();
		if(user==null) {
			throw new UsernameNotFoundException(id);
		}
		return user;
	}
	
	public boolean existsByUid(String uid){
		return !userRepository.existsByUid(uid);
	}
	
	public boolean existsByNick(String nick){
		return !userRepository.existsByNick(nick);
	}
	
}
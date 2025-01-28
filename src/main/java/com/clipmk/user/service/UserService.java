package com.clipmk.user.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clipmk.auth.dto.RegisterReqDto;
import com.clipmk.user.dto.EditUserReqDto;
import com.clipmk.user.dto.Role;
import com.clipmk.user.entity.User;
import com.clipmk.user.repository.UserRepository;
import com.global.error.errorCode.UserErrorCode;
import com.global.error.exception.RestApiException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{
	
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public int register(RegisterReqDto request) {
		
		if (userRepository.existsByUidOrNickOrEmail(request.getUid(), request.getNick(), request.getEmail())) {
			throw new RestApiException(UserErrorCode.DUPLICATE_USER);
	    }
		
		var user=User.builder()
				.uid(request.getUid())
				.pwd(passwordEncoder.encode(request.getPwd()))
				.nick(request.getNick())
				.email(request.getEmail())
				.emailReceive(request.getEmailReceive())
				.role(Role.USER)
				.point(0)
				.build();
		User savedUser = userRepository.save(user);
		
		return savedUser.getId();
	}
	
	public void edit(EditUserReqDto request, User user) {
		
		String newNick=request.getNick();
		if(newNick != null) {
			throw new RestApiException(UserErrorCode.NICK_NOT_FOUND);
		}
		
		user.setNick(newNick);
		userRepository.save(user);
	
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
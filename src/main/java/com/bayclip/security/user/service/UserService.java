package com.bayclip.security.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bayclip.security.user.entity.User;
import com.bayclip.security.user.repository.UserRepository;

@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public User loadUserByUsername(String uid) throws UsernameNotFoundException{
		User user=repository.findByUid(uid).orElseThrow();
		if(user==null) {
			throw new UsernameNotFoundException(uid);
		}
		return user;
	}
}
package com.bayclip.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bayclip.user.entity.User;
import com.bayclip.user.repository.UserRepository;

@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public User loadUserByUsername(String id) throws UsernameNotFoundException{
		User user=repository.findById(Integer.parseInt(id)).orElseThrow();
		if(user==null) {
			throw new UsernameNotFoundException(id);
		}
		return user;
	}
}
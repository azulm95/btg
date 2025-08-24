package com.co.btg.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.co.btg.api.dto.UserRequest;
import com.co.btg.api.exceptions.UserNotFoundException;
import com.co.btg.api.models.User;
import com.co.btg.api.repositories.GenericRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final GenericRepository<User> userRepository;
	
	
	public User saveUser(UserRequest dto) {
		User user = new User();
		if(dto.getUserId() == null) {
			user.setUserId(UUID.randomUUID().toString().substring(0, 8));
		}
		user.setBalance(dto.getBalance());
		user.setEmail(dto.getEmail());
		user.setName(dto.getName());
		user.setPhone(dto.getPhone());
		user.setPreferredNotification(dto.getPreferredNotification());
		return userRepository.save(user);
		
	}
	
	
	public User findById(String userId) throws UserNotFoundException {
		return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id: " + userId));
	}

}

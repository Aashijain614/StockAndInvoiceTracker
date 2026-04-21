package com.project.stock.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.stock.entity.User;
import com.project.stock.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	
	
	public void saveUser(User user)
	{
		userRepository.save(user);
	}

	
	public User loginUser(String email, String password) {
		
		Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user.get(); // login success
        }

        return null; // login fail
	}

}

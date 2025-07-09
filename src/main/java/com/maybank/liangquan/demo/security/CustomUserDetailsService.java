package com.maybank.liangquan.demo.security;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.maybank.liangquan.demo.object.User;
import com.maybank.liangquan.demo.object.UserCredential;
import com.maybank.liangquan.demo.repository.UserCredentialRepository;
import com.maybank.liangquan.demo.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

	private final UserRepository userRepository;

	private final UserCredentialRepository userCredentialRepository;

	@Autowired
	public CustomUserDetailsService(UserRepository userRepository, UserCredentialRepository userCredentialRepository) {
		this.userRepository = userRepository;
		this.userCredentialRepository = userCredentialRepository;
	}

	// UserId = username
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		LOGGER.debug("loadUserByUsername : {}", userId);
		UserCredential uc = userCredentialRepository.findByUsername(userId)
				.orElseThrow(() -> new UsernameNotFoundException("User not found."));
		User user = uc.getUser();

		return new org.springframework.security.core.userdetails.User(user.getEmail(), uc.getPassword(),
				Collections.emptyList());

	}

}

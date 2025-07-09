package com.maybank.liangquan.demo.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.maybank.liangquan.demo.exception.DuplicateUserException;
import com.maybank.liangquan.demo.exception.InvalidTokenException;
import com.maybank.liangquan.demo.object.AuthRequest;
import com.maybank.liangquan.demo.object.AuthResponse;
import com.maybank.liangquan.demo.object.RefreshTokenRequest;
import com.maybank.liangquan.demo.object.RegisterRequest;
import com.maybank.liangquan.demo.object.Token;
import com.maybank.liangquan.demo.object.User;
import com.maybank.liangquan.demo.object.UserCredential;
import com.maybank.liangquan.demo.repository.TokenRepository;
import com.maybank.liangquan.demo.repository.UserCredentialRepository;
import com.maybank.liangquan.demo.repository.UserRepository;
import com.maybank.liangquan.demo.security.JwtTokenProvider;
import com.maybank.liangquan.demo.utils.DateTimeUtils;

@Service
public class AuthService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

	@Value("${security.jwt.token.expire.period}")
	private long validityInMilliseconds;

	private final UserRepository userRepository;
	private final UserCredentialRepository userCredentialRepository;
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final AuthenticationManager authenticationManager;

	@Autowired
	private AuthService(UserRepository userRepo, UserCredentialRepository userCredentialRepo, TokenRepository tokenRepo,
			PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, AuthenticationManager authManager) {
		this.userRepository = userRepo;
		this.userCredentialRepository = userCredentialRepo;
		this.tokenRepository = tokenRepo;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
		this.authenticationManager = authManager;
	}

	public AuthResponse login(AuthRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUserId(), request.getPassword()));

		UserCredential uc = userCredentialRepository.findByUsername(request.getUserId())
				.orElseThrow(() -> new UsernameNotFoundException("User not found."));
		User user = uc.getUser();

		Date now = new Date();
		Date endingPeriod = new Date(now.getTime() + validityInMilliseconds);
		String token = jwtTokenProvider.createToken(uc.getUsername(), user.getEmail(), user.getMobileNo(),
				user.getFullName(), List.of("USER"), now, endingPeriod);
		String refreshToken = jwtTokenProvider.createToken(uc.getUsername(), user.getEmail(), user.getMobileNo(),
				user.getFullName(), List.of("USER"), now, endingPeriod);

		LOGGER.debug("Show User : {}", user.getUuid());
		LOGGER.debug("Show Username from Access Token : ", jwtTokenProvider.getUsername(token));
		LOGGER.debug("Show Username from Refresh Token : ", jwtTokenProvider.getUsername(refreshToken));
		saveUserToken(user, token, refreshToken, user.getUuid(),
				DateTimeUtils.convertDateToLocalDateTime(endingPeriod));

		return new AuthResponse(token, refreshToken);
	}

	public AuthResponse refreshToken(RefreshTokenRequest request) {

		if (jwtTokenProvider.validateToken(request.getRefreshToken())) {
			Token token = tokenRepository.findByRefreshToken(request.getRefreshToken())
					.orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));

			Date now = new Date();
			Date endingPeriod = new Date(now.getTime() + validityInMilliseconds);

			UserCredential uc = userCredentialRepository
					.findByUsername(jwtTokenProvider.getUsername(request.getRefreshToken()))
					.orElseThrow(() -> new UsernameNotFoundException("User not found."));

			String newToken = jwtTokenProvider.createToken(uc.getUsername(), uc.getUser().getEmail(),
					uc.getUser().getMobileNo(), uc.getUser().getFullName(), List.of("USER"), now, endingPeriod);
			token.setToken(newToken);
			tokenRepository.save(token);

			return new AuthResponse(newToken, request.getRefreshToken());
		}
		throw new InvalidTokenException("Refresh token was expired or invalid");
	}

	public void register(RegisterRequest request) {
		UserCredential searchResult = userCredentialRepository.findByUsername(request.getUserId()).orElse(null);

		if (searchResult != null) {
			throw new DuplicateUserException("User already exists");
		} else {
			LOGGER.debug("User entering are totally new!");
		}

		LOGGER.debug("Lets Create New User!");
		User user = new User(request.getFullName(), request.getEmail(), request.getMobileNo(), "System",
				LocalDateTime.now());
		user.setStatus("ACTIVE");

		UserCredential userCredential = new UserCredential();
		userCredential.setUsername(request.getUserId());
		userCredential.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setUserCredential(userCredential);
		userCredential.setUser(user);

		userRepository.saveAndFlush(user);
	}

	public void logout(String authHeader) {
		String jwt = authHeader.substring(7);
		Token token = tokenRepository.findByToken(jwt).orElseThrow(() -> new InvalidTokenException("Invalid token"));
		tokenRepository.delete(token);
	}

	private void saveUserToken(User user, String jwtToken, String refreshToken, String user_uuid,
			LocalDateTime expiredOn) {
		Token token = new Token(jwtToken, refreshToken, "System", LocalDateTime.now());
		token.setUserUuid(user_uuid);
		token.setExpiredOn(expiredOn);
		tokenRepository.save(token);
	}

}

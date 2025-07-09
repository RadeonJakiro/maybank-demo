package com.maybank.liangquan.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maybank.liangquan.demo.object.AuthRequest;
import com.maybank.liangquan.demo.object.AuthResponse;
import com.maybank.liangquan.demo.object.RefreshTokenRequest;
import com.maybank.liangquan.demo.object.RegisterRequest;
import com.maybank.liangquan.demo.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	private final AuthService authService;

	@Autowired
	AuthController(AuthService authService) {
		this.authService = authService;
	}

	@ResponseBody
	@RequestMapping(value = "/sample", method = RequestMethod.GET)
	private String entry() {
		return "Hello World";
	}

	@ResponseBody
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	private ResponseEntity<String> register(@RequestBody RegisterRequest request) {
		LOGGER.debug("Show Your Request During Registration : {}", request.getUserId());
		authService.register(request);
		return ResponseEntity.ok("Register Successfully!");
	}

	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	private ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

		AuthResponse resp = authService.login(request);
		return ResponseEntity.ok(resp);
	}

	@ResponseBody
	@RequestMapping(value = "/refresh-token", method = RequestMethod.POST)
	private ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
		AuthResponse resp = authService.refreshToken(request);
		return ResponseEntity.ok(resp);
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	private String logout(@RequestHeader("Authorization") String authHeader) {
		authService.logout(authHeader);
		return "Logout Succesfully";
	}

}
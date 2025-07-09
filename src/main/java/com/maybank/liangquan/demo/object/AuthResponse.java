package com.maybank.liangquan.demo.object;

public class AuthResponse {

	private String accessToken;

	private String refreshToken;

	public AuthResponse() {
	}

	public AuthResponse(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public AuthResponse setAccessToken(String accessToken) {
		this.accessToken = accessToken;
		return this;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public AuthResponse setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
		return this;
	}

}

package com.maybank.liangquan.demo.exception;

public class InvalidTokenException extends RuntimeException {

	private static final long serialVersionUID = -3382257047635815415L;

	public InvalidTokenException(String error) {
		super("Invalid Token Detected : " + error);
	}
}

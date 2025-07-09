package com.maybank.liangquan.demo.exception;

public class DuplicateUserException extends RuntimeException {

	private static final long serialVersionUID = -7796205712813049952L;

	public DuplicateUserException(String error) {
		super("Duplicate User Detected : " + error);
	}
}

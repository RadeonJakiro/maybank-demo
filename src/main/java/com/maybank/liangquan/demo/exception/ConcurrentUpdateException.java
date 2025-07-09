package com.maybank.liangquan.demo.exception;

public class ConcurrentUpdateException extends RuntimeException {

	private static final long serialVersionUID = 2011128077078577404L;

	public ConcurrentUpdateException(String error) {
		super("Concurrent Action Detected : " + error);
	}
}

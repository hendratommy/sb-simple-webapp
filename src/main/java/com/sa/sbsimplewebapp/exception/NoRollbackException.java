package com.sa.sbsimplewebapp.exception;

public class NoRollbackException extends Exception {
	public NoRollbackException(String message) {
		super(message);
	}
	
	public NoRollbackException(String message, Throwable throwable) {
		super(message, throwable);
	}

	private static final long serialVersionUID = 1L;

}

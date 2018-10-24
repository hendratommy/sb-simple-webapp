package com.sa.sbsimplewebapp.exception;

public class SystemServiceInstallException extends RuntimeException {
	public SystemServiceInstallException(String message) {
		super(message);
	}
	
	public SystemServiceInstallException(String message, Throwable throwable) {
		super(message, throwable);
	}

	private static final long serialVersionUID = 1L;

}

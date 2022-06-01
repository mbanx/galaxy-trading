package org.mbanx.challenge.galaxy.exception;

public class InvalidNumberFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidNumberFormatException() {
		super("Requested number is in invalid format");
	}
	
	public InvalidNumberFormatException(String message) {
		super(message);
	}
	
	
}

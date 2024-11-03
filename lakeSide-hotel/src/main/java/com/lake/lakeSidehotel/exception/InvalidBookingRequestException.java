package com.lake.lakeSidehotel.exception;

public class InvalidBookingRequestException extends RuntimeException {
	public InvalidBookingRequestException(String message) {
		super(message);
	}
}

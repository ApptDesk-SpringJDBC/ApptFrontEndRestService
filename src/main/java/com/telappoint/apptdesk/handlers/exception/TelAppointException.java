package com.telappoint.apptdesk.handlers.exception;

import org.springframework.http.HttpStatus;

/**
 * 
 * @author Balaji N
 *
 */
public class TelAppointException extends Exception {
	public String code;
	public String message;
	public HttpStatus httpStatus;
	public String exceptionMessage;
	public Object inputData;
	
	public TelAppointException(String code, String message,  HttpStatus httpStatus, Object inputData) {
		setCode(code);
		setMessage(message);
		setHttpStatus(httpStatus);
		setInputData(inputData);
	}

	public TelAppointException(String code, String message,  HttpStatus httpStatus, String exceptionMessage, Object inputData) {
		setCode(code);
		setMessage(message);
		setHttpStatus(httpStatus);
		setInputData(inputData);
		setExceptionMessage(exceptionMessage);
	}
	
	public TelAppointException(String code, String message,  HttpStatus httpStatus, String exceptionMessage, Exception e, Object inputData) {
		super(e);
		setCode(code);
		setMessage(message);
		setHttpStatus(httpStatus);
		setInputData(inputData);
		setExceptionMessage(exceptionMessage);
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public TelAppointException() {

	}


	public Object getInputData() {
		return inputData;
	}

	public void setInputData(Object inputData) {
		this.inputData = inputData;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
}

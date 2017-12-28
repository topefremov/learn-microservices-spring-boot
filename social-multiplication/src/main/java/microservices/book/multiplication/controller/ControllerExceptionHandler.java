package microservices.book.multiplication.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import microservices.book.multiplication.common.RegexUtility;
import microservices.book.multiplication.domain.InvalidJsonError;

@ControllerAdvice
public class ControllerExceptionHandler {
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<InvalidJsonError> httpMessageNotReadableExceptionHandler(HttpServletRequest req, HttpMessageNotReadableException ex) {
		InvalidJsonError jsonInvalidError = new InvalidJsonError(
				HttpStatus.BAD_REQUEST.value(), 
				"Bad Request", 
				RegexUtility.getFirstMatch("^[^;]*", ex.getMessage()));
		return ResponseEntity.badRequest().body(jsonInvalidError);
	}
}

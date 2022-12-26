package com.fampill.api.advice;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fampill.api.exception.DefaultException;
import com.fampill.api.response.FampillResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExampleControllerAdvice {

	/**
	 * Exception 발생에 예외처리
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public FampillResponse<?> handleException(Exception e, HttpServletRequest request) {
		log.error("handleException", e);
		// 응답값을 json 포맷으로 처리
		log.info("해당 조건에는 json으로 응답처리");
		return new FampillResponse<>(e.getMessage());
	}
	
	/**
	 * BindException 발생에 예외처리
	 * @param e
	 * @return
	 */
	@ExceptionHandler(BindException.class)
	public FampillResponse<?> handleBindException(BindException e, 
			HttpServletRequest request) {
		log.error("handleBindException", e);
		FieldError fieldError = e.getFieldError();
		// 응답값을 json 포맷으로 처리
		log.info("해당 조건에는 json으로 응답처리");
		return new FampillResponse<>(fieldError.getDefaultMessage());
	}
	
	/**
	 * DefaultException 발생에 예외처리
	 * @param e
	 * @return
	 */
	@ExceptionHandler(DefaultException.class)
	public FampillResponse<?> handleDefaultException(DefaultException e) {
		log.error("handleDefaultException", e);
		// 응답값을 json 포맷으로 처리
		log.info("해당 조건에는 json으로 응답처리");
		return new FampillResponse<>(e.getMessage());
	}
	
	
	/**
	 * ConstraintViolationException 발생에 예외처리
	 * @param e
	 * @return
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public FampillResponse<?> handleConstraintViolationException(ConstraintViolationException e) {
		log.error("handleConstraintViolationException", e);
		Iterator<ConstraintViolation<?>> iterator = e.getConstraintViolations().iterator();
		String message = null;
		while (iterator.hasNext()) {
			ConstraintViolation<?> violation = iterator.next();
			message = violation.getMessage();
			break;
		}
		// 응답값을 json 포맷으로 처리
		log.info("해당 조건에는 json으로 응답처리");
		return new FampillResponse<>(message);
	}
	
	
}

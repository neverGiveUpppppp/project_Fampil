package com.fampill.api.advice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fampill.api.exception.DefaultException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExampleControllerAdvice {

	private final MappingJackson2JsonView jsonView;
	
	/**
	 * Exception 발생에 예외처리
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception e, HttpServletRequest request) {
		log.error("handleException", e);
		String requested = request.getHeader("X-Requested-With");
		// 응답값을 json 포맷으로 처리
		if (requested != null && requested.equals("XMLHttpRequest")) {
			log.info("해당 조건에는 json으로 응답처리");
			ModelAndView view = new ModelAndView(jsonView);
			// 응답을 오류 상태로
			view.setStatus(HttpStatus.BAD_REQUEST);
			view.addObject("message", e.getMessage());
			return view;
		}
		ModelAndView view = new ModelAndView("/error/message.html");
		view.addObject("message", e.getMessage());		
		return view;
	}
	
	/**
	 * BindException 발생에 예외처리
	 * @param e
	 * @return
	 */
	@ExceptionHandler(BindException.class)
	public ModelAndView handleBindException(BindException e, 
			HttpServletRequest request) {
		log.error("handleBindException", e);
		FieldError fieldError = e.getFieldError();
		String requested = request.getHeader("X-Requested-With");
		// 응답값을 json 포맷으로 처리
		if (requested != null && requested.equals("XMLHttpRequest")) {
			log.info("해당 조건에는 json으로 응답처리");
			ModelAndView view = new ModelAndView(jsonView);
			// 응답을 오류 상태로
			view.setStatus(HttpStatus.BAD_REQUEST);
			view.addObject("message", fieldError.getDefaultMessage());
			return view;
		}
		
		ModelAndView view = new ModelAndView("/error/message.html");
		view.addObject("message", fieldError.getDefaultMessage());		
		return view;
	}
	
	/**
	 * DefaultException 발생에 예외처리
	 * @param e
	 * @return
	 */
	@ExceptionHandler(DefaultException.class)
	public ModelAndView handleDefaultException(DefaultException e) {
		log.error("handleDefaultException", e);
		return handle(e);
	}
	
	private ModelAndView handle(Exception e) {
		ModelAndView view = new ModelAndView("/error/error.html");
		view.addObject("exception", e);
		return view;
	}
}

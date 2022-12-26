package com.fampill.api.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultHandleInterceptor implements HandlerInterceptor {

	private static final SimpleGrantedAuthority ANONYMOUS = new SimpleGrantedAuthority("ROLE_ANONYMOUS");
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean anonymous = authentication.getAuthorities()
			.contains(ANONYMOUS);
		if (anonymous) {
			log.info("익명사용자 요청");
		} else {
			log.info("로그인 된 요청");
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex)
			throws Exception {
		log.info("afterCompletion : {}", request.getRequestURI());
	}
}

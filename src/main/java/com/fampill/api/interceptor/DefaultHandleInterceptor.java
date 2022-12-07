package com.fampill.api.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fampill.api.annotation.RequestConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultHandleInterceptor implements HandlerInterceptor {

	private static final SimpleGrantedAuthority ANONYMOUS = new SimpleGrantedAuthority("ROLE_ANONYMOUS");
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			RequestConfig config = handlerMethod.getMethodAnnotation(RequestConfig.class);
			if (config != null) {
				if (config.realname()) {
					Object realnameCheck = request.getSession().getAttribute("realnameCheck");
					boolean realname = realnameCheck != null ? (boolean) realnameCheck : false;
					if (!realname) {
						log.info("본인인증이 아직안되어서 접근이 불가능합니다.");
						throw new IllegalArgumentException("본인인증이 필요합니다.");
					}
				}
			}
		}
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
		log.info("postHandle : {}", request.getRequestURI());
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			RequestConfig config = handlerMethod.getMethodAnnotation(RequestConfig.class);
			if (config != null) {
				request.setAttribute("menu", config.menu());
			}
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex)
			throws Exception {
		log.info("afterCompletion : {}", request.getRequestURI());
	}
}

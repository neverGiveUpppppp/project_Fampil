package com.fampill.api.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class XssFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("filterConfig : {}", filterConfig);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, 
		FilterChain chain)
			throws IOException, ServletException {
		log.info("doFilter : {}", request.getParameter("title"));
		chain.doFilter(new RequestWrapper((HttpServletRequest) request), 
			response);
	}

}

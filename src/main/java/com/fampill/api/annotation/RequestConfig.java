package com.fampill.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestConfig {

	/**
	 * 현재 메뉴를 set
	 * @return
	 */
	String menu() default "";
	
	/**
	 * 실명인증이 필요한곳에서 true로 사용
	 * @return
	 */
	boolean realname() default false;

}

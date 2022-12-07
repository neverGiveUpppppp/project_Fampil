package com.fampill.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigruation {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			// 해당 url 패턴은 로그인 권한없어도 접근되게
			.antMatchers("/public/**", "/member/form", "/member/join**", "/", "/home")
			.permitAll()
			// 나머지 요청은 로그인을 해야 접근되게
			.anyRequest().hasRole("USER").and()
			//.csrf().disable()
			.formLogin()
			.defaultSuccessUrl("/home")
			.permitAll();
		return http.build();
	}
	
	/**
	 * 비밀번호 인코더 등록
	 * 등록안하면 There is no PasswordEncoder mapped for the id "null" 에러가 발생
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
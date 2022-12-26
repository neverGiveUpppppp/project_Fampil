package com.fampill.api.configuration;

import java.io.IOException;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fampill.api.mapper.MemberMapper;
import com.fampill.api.response.FampillResponse;
import com.fampill.api.security.JwtTokenAuthenticationManager;
import com.fampill.api.security.SecurityUserDetailsService;
import com.fampill.api.security.UsernamePasswordAuthenticationSuccessHandler;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity(debug = false)
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfigruation {

	private final MappingJackson2JsonView jsonView;
	private final MemberMapper memberMapper;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		JwtTokenAuthenticationManager authenticationManager = new JwtTokenAuthenticationManager(jwtDecoder(),
				securityUserDetailsService());
		BearerTokenAuthenticationFilter tokenAuthenticationFilter = new BearerTokenAuthenticationFilter(authenticationManager);
		tokenAuthenticationFilter.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
			
			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
				log.error("BearerTokenAuthenticationFilter onAuthenticationFailure", exception);
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				response.setCharacterEncoding("UTF-8");
				response.setContentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE);				
				jsonView.getObjectMapper().writer().writeValue(response.getOutputStream(),
						new FampillResponse<>(exception.getMessage()));
				
			}
		});
		http.authorizeRequests()
			// 해당 url 패턴은 로그인 권한없어도 접근되게
			.antMatchers("/public/**", 
				"/member/form", "/member/join**", "/member/phone-auth", "/", 
				"/member/phone-auth","/member/id-check", "/", "/home",
				"/member/phone-authcheck",
				// security에서는 아래 url 인증안되도 접속되게 권한 설정
				"/v2/api-docs",
	            "/swagger-resources",
	            "/swagger-resources/**",
	            "/configuration/ui",
	            "/configuration/security",
	            "/swagger-ui.html",
	            "/webjars/**",
	            /* swagger v3 */
	            "/v3/api-docs/**",
	            "/swagger-ui/**")
			.permitAll()
			// 나머지 요청은 로그인을 해야 접근되게
			.anyRequest().hasRole("USER").and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.cors().configurationSource(corsConfigurationSource())
			.and()
				.csrf().disable()
			.formLogin()
			.successHandler(new UsernamePasswordAuthenticationSuccessHandler(jwtEncoder(), jsonView))
			.failureHandler(new AuthenticationFailureHandler() {
				
				@Override
				public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
						AuthenticationException exception) throws IOException, ServletException {
					log.error("onAuthenticationFailure", exception);
					response.setStatus(HttpStatus.UNAUTHORIZED.value());
					response.setCharacterEncoding("UTF-8");
					response.setContentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE);					
					jsonView.getObjectMapper().writer().writeValue(response.getOutputStream(),
							new FampillResponse<>("인증이 실패하였습니다."));
				}
			})
			.permitAll()
			.and()
				.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
					
					@Override
					public void handle(HttpServletRequest request, HttpServletResponse response,
							AccessDeniedException accessDeniedException) throws IOException, ServletException {
					}
				})
				.authenticationEntryPoint(new AuthenticationEntryPoint() {
					
					@Override
					public void commence(HttpServletRequest request, HttpServletResponse response,
							AuthenticationException exception) throws IOException, ServletException {
						response.setStatus(HttpStatus.UNAUTHORIZED.value());
						response.setCharacterEncoding("UTF-8");
						response.setContentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
						jsonView.getObjectMapper().writer().writeValue(response.getOutputStream(),
								new FampillResponse<>("로그인 인증이 필요한 API 입니다."));
					}
				});
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
	
	@Value("${jwt.secret-key}")
	private String secretKey;
	
	@Bean
	public JwtEncoder jwtEncoder() {
		return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey.getBytes()));
	}
	
	@Bean
	public JwtDecoder jwtDecoder() {
		SecretKeySpec spec = new SecretKeySpec(secretKey.getBytes(), JWSAlgorithm.HS256.getName());
		return NimbusJwtDecoder.withSecretKey(spec).build();
	}
	
	@Bean
	public SecurityUserDetailsService securityUserDetailsService() {
		return new SecurityUserDetailsService(memberMapper);
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.addAllowedOriginPattern("*");
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
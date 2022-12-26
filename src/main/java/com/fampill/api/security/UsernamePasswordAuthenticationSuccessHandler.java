package com.fampill.api.security;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fampill.api.response.FampillResponse;
import com.fampill.api.response.LoginInfo;
import com.fampill.api.response.LoginResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationSuccessHandler
 implements AuthenticationSuccessHandler {
	
	private final JwtEncoder encoder;
	private final MappingJackson2JsonView jsonView;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		Object credentials = authentication.getCredentials();
		String name = authentication.getName();
		SecurityUserDetails  principal = (SecurityUserDetails) authentication.getPrincipal();
		log.debug("credentials : {}", credentials);
		log.debug("name : {}", name);
		log.debug("principal : {}", principal);
		
		JwtClaimsSet claimsSet = JwtClaimsSet.builder()
			.subject("FAMPILL_TOKEN")
			.expiresAt(Instant.now().plusSeconds(60 * 60 * 24 * 30)) //60*60*24 하루 * 30 한달 토큰유지
			.claim("memberSeq", principal.getMemberSeq())
			.build();
		Jwt jwt = encoder.encode(JwtEncoderParameters.from(
				JwsHeader.with(MacAlgorithm.HS256).build(),
				claimsSet));
		
		String tokenValue = jwt.getTokenValue();
		log.debug("tokenValue : {}", tokenValue);
		
		
		LoginResponse loginResponse = new LoginResponse();
		
		LoginInfo loginInfo = new LoginInfo();
		
		
		loginInfo.setUsername(principal.getName());
		
		loginResponse.setLoginInfo(loginInfo);
		loginResponse.setToken(tokenValue);
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.OK.value());
		
		jsonView.getObjectMapper().writer().writeValue(response.getOutputStream(),
				new FampillResponse<LoginResponse>(loginResponse));
		
	}

}

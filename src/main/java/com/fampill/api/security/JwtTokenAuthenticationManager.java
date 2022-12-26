package com.fampill.api.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import com.fampill.api.domain.Member;
import com.fampill.api.exception.DefaultException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenAuthenticationManager implements AuthenticationManager {

	private final JwtDecoder decoder;
	private final SecurityUserDetailsService securityUserDetailsService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String tokenValue = authentication.getPrincipal().toString();
		try {
			Jwt jwt = decoder.decode(tokenValue);
			long memberSeq = jwt.getClaim("memberSeq");
			Member member = securityUserDetailsService.selectMemberByMemberSeq((int) memberSeq);
			if (member == null) {
				throw new DefaultException("회원이 존재하지 않습니다.");
			}
			List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
			return new UsernamePasswordAuthenticationToken(SecurityUserDetails.builder()
				.memberSeq(member.getMemberSeq())
				.username(member.getUserId())
				.name(member.getUsername())
				.password(member.getPassword())
				.authorities(authorities)
				.build(), 
				member.getPassword(),
				authorities);
		} catch (JwtException e) {
			log.error("e", e);
			throw new DefaultException("토큰이 만료되었습니다.");
		}
	}

}

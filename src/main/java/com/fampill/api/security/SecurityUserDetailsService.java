package com.fampill.api.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.fampill.api.domain.Member;
import com.fampill.api.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class SecurityUserDetailsService implements UserDetailsService {
	
	private final MemberMapper memberMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("loadUserByUsername : {}", username);
		Member member = memberMapper.selectMemberAccount(username);
		if (member == null) {
			throw new UsernameNotFoundException("회원이 존재하지 않습니다.");
		}
		log.info("member : {}", member);
		return SecurityUserDetails.builder()
			.memberSeq(member.getMemberSeq())
			.username(username)
			.name(member.getUsername())
			.password(member.getPassword())
			.build();
	}
	
	public Member selectMemberByMemberSeq(int memberSeq) {
		return memberMapper.selectMemberByMemberSeq(memberSeq);
	}
	
}

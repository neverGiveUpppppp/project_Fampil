package com.fampill.api.mapper;

import com.fampill.api.domain.Member;

public interface MemberMapper {

	int selectMemberAccountCount(String account);
	
	void insertMember(Member member);

	Member selectMemberAccount(String username);
	
}

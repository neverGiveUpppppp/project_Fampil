package com.fampill.api.mapper;

import java.util.Map;

import com.fampill.api.domain.Member;

public interface MemberMapper {

	void insertAuthNum(Map<String, Object> authInfo);

	String selectAuthNum(String hp);

	int selectMemberUserIdCount(String userId);
	
	int selectPhoneNumberChk(String hp);
	
	//////////////////////////////////////////
	
	int selectMemberAccountCount(String account);
	
	void insertMember(Member member);

	Member selectMemberAccount(String username);
	
	Member selectMemberByMemberSeq(int memberSeq);

	String selectAuthNum(Map<String, Object> authInfo);

	



}

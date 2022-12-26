package com.fampill.api.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fampill.api.controller.form.MemberJoinForm;
import com.fampill.api.domain.Member;
import com.fampill.api.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;
	

	public void insertAuthNum(Map<String, Object> authInfo) {
		memberMapper.insertAuthNum(authInfo);
		
	}
	
	public String selectAuthNum(String hp) {
		return memberMapper.selectAuthNum(hp);
		
	}
	// 계정 중복체크
	public int selectMemberUserIdCount(String userId) {
		return memberMapper.selectMemberUserIdCount(userId);
	}
	public void insertMember(MemberJoinForm form) {
		Member member = new Member();
		
		member.setUserId(form.getUserId());
		String encodePassword = passwordEncoder.encode(form.getPassword());
		member.setPassword(encodePassword);
		member.setUsername(form.getUsername());
		member.setHp(form.getHp());
		member.setAgree1(form.isAgree1());
		member.setAgree2(form.isAgree2());
		member.setAgree3(form.isAgree3());
		
		memberMapper.insertMember(member);
	}
	
	public int selectPhoneNumberChk(String hp) {
		return memberMapper.selectPhoneNumberChk(hp);
	}

	///////////////////////////////////////////////
	
	@Value("${file.root-path}")
	private String rootPath;




	/*
	public void insertMember(MemberJoinForm form) {
		log.info("insertMember form : {}", form);
		MultipartFile profileImage = form.getProfileImage();
		String originalFilename = profileImage.getOriginalFilename();
		String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, 
				originalFilename.length());
		String randomFilename = UUID.randomUUID().toString() + "." + ext; 
		String addPath = "/" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
		// 저장경로
		String savePath = new StringBuilder(rootPath).append(addPath).toString();
		log.info("savePath : {}", savePath);
		String imagePath = addPath + "/" + randomFilename;
		File saveDir = new File(savePath);
		log.info("originalFilename : {}", originalFilename);
		log.info("ext : {}", ext);
		log.info("randomFilename : {}", randomFilename);
		// 폴더가 없는경우 
		if (!saveDir.isDirectory()) {
			// 폴더 생성
			saveDir.mkdirs();
		}
		File out = new File(saveDir, randomFilename);
		try {
			FileCopyUtils.copy(profileImage.getInputStream(), new FileOutputStream(out));
		} catch (IOException e) {
			log.error("fileCopy", e);
			throw new RuntimeException("파일을 저장하는 과정에 오류가 발생하였습니다.");
		}
		Member member = new Member();

		member.setAccount(form.getAccount());
		
		String encodePassword = passwordEncoder.encode(form.getPassword());
		log.info("encodePassword : {}", encodePassword);
		
		member.setPassword(encodePassword);
		member.setNickname(form.getNickname());
		member.setProfileImagePath(imagePath);
		member.setProfileImageName(originalFilename);
		
		memberMapper.insertMember(member);
	}
*/


	
}

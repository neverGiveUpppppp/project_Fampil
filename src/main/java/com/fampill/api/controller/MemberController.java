package com.fampill.api.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fampill.api.annotation.RequestConfig;
import com.fampill.api.controller.form.MemberJoinForm;
import com.fampill.api.exception.DefaultException;
import com.fampill.api.response.FampillResponse;
import com.fampill.api.service.MemberService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//1. @Api어노테이션으로 컨트롤러에 설명을 추가합니다.
@Api(tags = { "회원정보 REST API" })
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
@Validated 
public class MemberController {

	private final MemberService memberService;
	
	@ApiOperation(value = "핸드폰번호 인증")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "hp", type = "string", value = "휴대폰번호")
	})		
	@PostMapping("/phone-auth")
	@RequestConfig(menu = "MEMBER")
	public FampillResponse<?> phoneAuth(@RequestParam(required = false) String hp) {
		Assert.hasLength(hp, "핸드폰번호를 입력해주세요.");
		String regEx = "(\\d{2,3})(\\d{3,4})(\\d{4})";
		if (!Pattern.matches(regEx, hp)) {
			throw new DefaultException("숫자로 정확히 입력해주세요.");
		}
		
		//전화번호중복체크
		boolean isUsePhoneNumber = memberService.selectPhoneNumberChk(hp) > 0;
		Assert.state(!isUsePhoneNumber, "이미 등록된 전화번호입니다.");
		
		// 성공인경우
		Map<String, Object> result = new HashMap<>();
		//인증번호 생성
		String authNum = RandomStringUtils.randomNumeric(6);

		//인증번호를 db에 넣어준다.
		Map<String, Object> authInfo = new HashMap<>();
		authInfo.put("hp", hp);
		authInfo.put("authNum", authNum);
		memberService.insertAuthNum(authInfo);
		
		result.put("authNumber", authNum);
		return new FampillResponse<>(result);
	}
	
	@ApiOperation(value = "핸드폰번호 인증확인")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "hp", type = "string", value = "휴대폰번호"),
		@ApiImplicitParam(name = "authNum", type = "string", value = "인증번호")
	})	
	@PostMapping("/phone-authcheck")
	@RequestConfig(menu = "MEMBER")
	public FampillResponse<?> phoneAuthCheck(@RequestParam String authNum, @RequestParam String hp) {
		Assert.hasLength(authNum, "인증번호를 입력해주세요.");
		String authNumCheck = memberService.selectAuthNum(hp);
		log.info("authNumCheck : {}", authNumCheck);
		//db에 있는 auth값과 일치하는지 확인한다.
		if (authNumCheck != null && !authNumCheck.equals(authNum)) {
			throw new DefaultException("인증번호가 일치하지 않습니다.");
		}
		return new FampillResponse<>(HttpStatus.OK);
	}
	
	@ApiOperation(value = "아이디 중복체크")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userId", type = "string", value = "아이디")
	})
	@PostMapping("/id-check")
	@RequestConfig(menu = "MEMBER")
	public FampillResponse<?> idCheck(
			@RequestParam @Email(message = "이메일을 올바르게 입력해주세요.") String userId) {
		
		// 계정 중복체크
		boolean isUseAccount = memberService.
			selectMemberUserIdCount(userId) > 0;
		Assert.state(!isUseAccount, "이미 가입된 이메일입니다.");
		
		Map<String, Object> result = new HashMap<>();
		result.put("idCheck", "사용 가능한 이메일 입니다.");
		
		return new FampillResponse<>(result);
	}
	
	
	@ApiOperation(value = "회원가입 등록")
	@PostMapping("/join")
	@RequestConfig(menu = "MEMBER", realname = true)
	public FampillResponse<?> join(@Validated MemberJoinForm form) {
		
		//전화번호중복체크
		boolean isUsePhoneNumber = memberService.selectPhoneNumberChk(form.getHp()) > 0;
		Assert.state(!isUsePhoneNumber, "이미 등록된 전화번호입니다.");
		
		// 계정 중복체크
		boolean isUseAccount = memberService.
			selectMemberUserIdCount(form.getUserId()) > 0;
		Assert.state(!isUseAccount, "이미 가입된 이메일입니다.");
		// 회원가입 정보 DB에 등록
		memberService.insertMember(form);

		return new FampillResponse<>(HttpStatus.OK);
	}
	
}

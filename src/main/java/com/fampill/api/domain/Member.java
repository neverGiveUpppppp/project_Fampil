package com.fampill.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

//1. @ApiModel 어노테이션으로 모델에 설명을 추가합니다
@ApiModel(value = "MemberDto", description = "회원정보")
@Data
public class Member {

	// 2. @ApiModelProperty 어노테이션 모델의 요소에 설명을 추가합니다.
	@ApiModelProperty(value = "회원 번호")
	private int memberSeq;

	@ApiModelProperty(value = "아이디")
	private String userId;

	@ApiModelProperty(value = "비밀번호")
	private String password;

	@ApiModelProperty(value = "회원 이름")
	private String username;

	@ApiModelProperty(value = "핸드폰번호")
	private String hp;

	@ApiModelProperty(value = "회원 가입 날짜")
	private String joinDate;

	@ApiModelProperty(value = "약관1")
	private boolean agree1;

	@ApiModelProperty(value = "약관2")
	private boolean agree2;

	@ApiModelProperty(value = "약관3")
	private boolean agree3;
}

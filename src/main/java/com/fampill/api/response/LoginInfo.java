package com.fampill.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "LoginInfo", description = "로그인 회원정보")
@Data
public class LoginInfo {

	@ApiModelProperty(value = "회원 이름")
	private String username;

}

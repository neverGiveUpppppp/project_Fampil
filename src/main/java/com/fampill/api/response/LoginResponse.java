package com.fampill.api.response;

import lombok.Data;

@Data
public class LoginResponse {

	private String token;
	private LoginInfo loginInfo;
	
}

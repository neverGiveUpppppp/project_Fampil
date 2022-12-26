package com.fampill.api.response;

import lombok.Data;

@Data
public class FampillResponse<T> {

	private boolean success = false; // 성공여부
	private T data; // 성공시 응답 데이터
	private String message;
	
	/**
	 * API 응답 성공시 클라이언트에 내려줄 때 사용
	 * @param data
	 */
	public FampillResponse(T data) {
		this.success = true;
		this.data = data;
	}
	

	/**
	 * API 응답 실패시 클라이언트에 내려줄 때 사용
	 * @param data
	 */
	public FampillResponse(String message) {
		this.success = false;
		this.message = message;
	}
	
}

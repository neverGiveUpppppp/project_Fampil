package com.fampill.api.domain;

import lombok.Data;

@Data
public class Board {
	
	private int boardSeq;
	private BoardType boardType;
	private String title;
	private String contents;
	private String regDate;
	private String userName; // 회원 이름
	private int memberSeq;
	
}

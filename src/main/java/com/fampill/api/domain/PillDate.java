package com.fampill.api.domain;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "MemberDto", description = "메인화면 영양제 날짜정보")
@Data
public class PillDate {
	
	@ApiModelProperty(value = "날짜포맷 YYYYMMDD")
	private String date; // 20121212
	
	@ApiModelProperty(value = "날짜포맷 MM월 DD일")
	private String dateLabel; // 12월 12일 
	
	@ApiModelProperty(value = "월화수목금토일")
	private String week; // 코드
	
	@ApiModelProperty(value = "한글로 월화수목금토일")
	private String weekLabel; // 한글로 월화수목금토일
	
	@ApiModelProperty(value = "현재 표시 용도")
	private boolean now; // 현재 표시 용도
	
	@ApiModelProperty(value = "섭취해온 날짜 dday")
	private int countDay; // 섭취해온 날짜
	
	@ApiModelProperty(value = "섭취시간이력")
	private List<FampillTime> timeList; //섭취이력
	
	@ApiModelProperty(value = "컨디션")
	private int condition; //컨디션
}

package test;

import lombok.Data;

@Data
public class NutrientsDate {

	private String date; // 20121212
	private String dateLabel; // 12월 12일 
	private String week; // 코드
	private String weekLabel; // 한글로 월화수목금토일
	private boolean now; // 현재 표시 용도
	private boolean take; // 섭취 O 표시 용도
	
}

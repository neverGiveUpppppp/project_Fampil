package com.fampill.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "FampillTimeDto", description = "영양제 섭취시간이력")
@Data
public class FampillTime {
	
	@ApiModelProperty(value = "섭취시간번호")
	private int timeNo;
	
	@ApiModelProperty(value = "내영양제 등록번호")
	private int regNo;
	
	@ApiModelProperty(value = "아이디")
	private String userId;
	
	@ApiModelProperty(value = "영양제 고유번호")
	private int pillNo;
	
	@ApiModelProperty(value = "오전false/오후true")
	private boolean hourtype = false;
	
	@ApiModelProperty(value = "섭취시간 HH")
	private String hour;
	
	@ApiModelProperty(value = "섭취시간 mm")
	private String minute;
	
	@ApiModelProperty(value = "섭취캡슐갯수")
	private int capsule;
	
	@ApiModelProperty(value = "영양제섭취여부")
	private boolean takeYn;
	
	@ApiModelProperty(value = "영양제섭취시간등록일자")
	private String regDate;
	
	@ApiModelProperty(value = "영양제 풀네임")
	private String pillFullName;
}

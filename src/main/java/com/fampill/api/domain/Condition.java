package com.fampill.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "ConditionDto", description = "컨디션")
@Data
public class Condition {
	
	@ApiModelProperty(value = "컨디션번호")
	private int conNo;
	
	@ApiModelProperty(value = "아이디")
	private String userId;
	
	@ApiModelProperty(value = "(int)상태 1나쁨 2보통 3좋음")
	private int status;
	
	@ApiModelProperty(value = "컨디션등록일자")
	private String regDate;
	
}

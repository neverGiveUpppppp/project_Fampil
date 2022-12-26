package com.fampill.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "FampillTakeDto", description = "영양제 섭취시간이력")
@Data
public class FampillTake {

	@ApiModelProperty(value = "섭취이력번호")
	private int takeNo;
	
	@ApiModelProperty(value = "내영양제등록번호")
	private int regNo;
	
	@ApiModelProperty(value = "아이디")
	private String userId;
	
	@ApiModelProperty(value = "섭취시간번호")
	private int timeNo;
	
	@ApiModelProperty(value = "섭취시간등록일자")
	private String regDate;
}

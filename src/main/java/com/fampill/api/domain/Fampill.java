package com.fampill.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="Fampill", description = "영양제정보")
@Data
public class Fampill {
	
	@ApiModelProperty(value="영양제번호")
	private int pillNO;
	
	@ApiModelProperty(value="영양제이름")
	private String pillName; 
	
	@ApiModelProperty(value="영양제사진")
	private String pillImg;
	
	@ApiModelProperty(value="제조사")
	private String mfr;
	
	@ApiModelProperty(value="내용량")
	private String capacity;
	
	@ApiModelProperty(value="상세이미지")
	private String pillImgDtl;
	
	@ApiModelProperty(value="영양제풀네임")
	private String pillFullName;
}

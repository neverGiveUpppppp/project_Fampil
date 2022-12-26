package com.fampill.api.controller.form;

import javax.validation.constraints.NotEmpty;

import com.fampill.api.validation.ValidationSteps;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PillSaveForm {
	
	@ApiModelProperty(value="등록번호")
	private int regNo;
	
	@ApiModelProperty(value="아이디")
	private String userId;
	
	@NotEmpty(groups = ValidationSteps.Step1.class, message = "{Form.pillNo.notEmpty}")
	@ApiModelProperty(value="영양제번호")
	private int pillNo;
	
	@ApiModelProperty(value="월")
	private boolean mon;
	
	@ApiModelProperty(value="화")
	private boolean tue;
	
	@ApiModelProperty(value="수")
	private boolean wed;
	
	@ApiModelProperty(value="목")
	private boolean thu;
	
	@ApiModelProperty(value="금")
	private boolean fri;
	
	@ApiModelProperty(value="토")
	private boolean sat;
	
	@ApiModelProperty(value="일")
	private boolean sun;
	
	@ApiModelProperty(value="오전(false)/오후(true)1")
	private boolean hourtype1 = false;
	
	@ApiModelProperty(value="시1")
	private String hour1;
	
	@ApiModelProperty(value="분1")
	private String minute1;
	
	@ApiModelProperty(value="복용량1")
	private	int capsule1;
	
	@ApiModelProperty(value="오전(false)/오후(true)2")
	private boolean hourtype2 = false;
	
	@ApiModelProperty(value="시2")
	private String hour2;
	
	@ApiModelProperty(value="분2")
	private String minute2;
	
	@ApiModelProperty(value="복용량2")
	private	int capsule2;
	
	@ApiModelProperty(value="오전(false)/오후(true)3")
	private boolean hourtype3 = false;
	
	@ApiModelProperty(value="시3")
	private String hour3;
	
	@ApiModelProperty(value="분3")
	private String minute3;
	
	@ApiModelProperty(value="복용량3")
	private	int capsule3;

	@ApiModelProperty(value="영양제이름")
	private String pillName;
	
	@ApiModelProperty(value="제조사이름")
	private String MFR;

}

package com.fampill.api.controller.form;

import javax.validation.GroupSequence;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fampill.api.validation.ValidationSteps;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@GroupSequence({
	MemberJoinForm.class,
	ValidationSteps.Step1.class,
	ValidationSteps.Step2.class,
	ValidationSteps.Step3.class,
	ValidationSteps.Step4.class,
	ValidationSteps.Step5.class,
	ValidationSteps.Step6.class,
	ValidationSteps.Step7.class,
})
@ApiModel(value = "MemberJoinForm", description = "회원 가입 폼")
public class MemberJoinForm {
	
	@NotEmpty(groups = ValidationSteps.Step1.class, message = "{MemberJoinForm.userId.notEmpty}")
	@Email(groups = ValidationSteps.Step1.class, message = "{MemberJoinForm.userId.pattern}")
	@ApiModelProperty(value = "아이디")
	private String userId;

	@NotEmpty(groups = ValidationSteps.Step2.class, message = "{MemberJoinForm.password.notEmpty}")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$",groups = ValidationSteps.Step4.class, message = "{MemberJoinForm.password.pattern}")
	@ApiModelProperty(value = "비밀번호")
	private String password;
	
	@NotEmpty(groups = ValidationSteps.Step3.class, message = "{MemberJoinForm.username.notEmpty}")
	@Length(groups = ValidationSteps.Step3.class, min = 2, max = 10, message = "{MemberJoinForm.username.length}")
	@ApiModelProperty(value = "이름")
	private String username;
	
	@NotEmpty(groups = ValidationSteps.Step4.class, message = "{MemberJoinForm.hp.notEmpty}")
	@Pattern(groups = ValidationSteps.Step4.class, regexp = "(\\d{2,3})(\\d{3,4})(\\d{4})", message = "{MemberJoinForm.hp.pattern}")
	@ApiModelProperty(value = "휴대폰번호")
	private String hp;
	
	@AssertTrue(groups = ValidationSteps.Step5.class, message = "{MemberJoinForm.agree1.notEmpty}")
	@ApiModelProperty(value = "이용약관")
	private boolean agree1;
	
	@AssertTrue(groups = ValidationSteps.Step6.class, message = "{MemberJoinForm.agree2.notEmpty}")
	@ApiModelProperty(value = "개인정보 수집/이용")
	private boolean agree2;
	
	@AssertTrue(groups = ValidationSteps.Step7.class, message = "{MemberJoinForm.agree3.notEmpty}")
	@ApiModelProperty(value = "14세 이상")
	private boolean agree3;
	/*
	@NotEmpty(groups = ValidationSteps.Step1.class, message = "{MemberSaveForm.account.notEmpty}")
	@Email(groups = ValidationSteps.Step2.class, message = "{MemberSaveForm.account.pattern}")
	private String account;

	@NotEmpty(groups = ValidationSteps.Step3.class, message = "{MemberSaveForm.password.notEmpty}")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,12}$",groups = ValidationSteps.Step4.class, message = "{MemberSaveForm.password.pattern}")
	private String password;
	
	@NotEmpty(groups = ValidationSteps.Step5.class, message = "{MemberSaveForm.nickname.notEmpty}")
	@Length(groups = ValidationSteps.Step6.class, min = 2, max = 10, message = "{MemberSaveForm.nickname.length}")
	private String nickname;
	
	@NotEmptyFile(groups = ValidationSteps.Step7.class, message = "{MemberSaveForm.profileImage.notEmpty}")
	private MultipartFile profileImage;
	*/
}

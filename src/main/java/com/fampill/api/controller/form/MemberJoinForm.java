package com.fampill.api.controller.form;

import javax.validation.GroupSequence;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import com.fampill.api.validation.NotEmptyFile;
import com.fampill.api.validation.ValidationSteps;

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
public class MemberJoinForm {
	
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
}

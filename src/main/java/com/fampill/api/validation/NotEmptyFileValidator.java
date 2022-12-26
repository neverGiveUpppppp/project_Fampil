package com.fampill.api.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class NotEmptyFileValidator implements ConstraintValidator<NotEmptyFile, MultipartFile> {

    @Override
    public void initialize(NotEmptyFile annotation) {}

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        return multipartFile != null && !multipartFile.isEmpty();
    }

}
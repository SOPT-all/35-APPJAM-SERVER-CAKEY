package com.cakey.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValueOfEnumValidator implements ConstraintValidator<EnumValue, String> {

    private EnumValue annotation;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false; // null 또는 빈 값은 유효하지 않음
        }

        Object[] enumValues = this.annotation.enumClass().getEnumConstants();
        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                // 대소문자 구분하여 비교
                if (value.equals(enumValue.toString())) {
                    return true;
                }
            }
        }
        // 검증 실패 메시지를 설정
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(this.annotation.message())
                .addConstraintViolation();
        return false;
    }
}

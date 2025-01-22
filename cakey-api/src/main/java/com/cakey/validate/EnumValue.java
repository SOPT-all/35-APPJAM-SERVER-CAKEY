package com.cakey.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = {ValueOfEnumValidator.class})
public @interface EnumValue {
    String message() default "Enum에 없는 값입니다. (대문자만 허용됨)";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    Class<? extends java.lang.Enum<?>> enumClass();
}
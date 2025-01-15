package com.ripple.BE.learning.validation;

import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AnswerIndexValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAnswerIndex {

    String message() default "Invalid answer index";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}

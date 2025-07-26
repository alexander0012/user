package com.test.user.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailFormatValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmailFormat {

    String message() default "El formato del correo electrónico no es válido.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
package com.test.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class EmailFormatValidator implements ConstraintValidator<ValidEmailFormat, String> {

    @Value("${validation.email.regex}")
    private String emailRegex;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) {
            return true;
        }
        return Pattern.compile(emailRegex).matcher(email).matches();
    }
}
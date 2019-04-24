package com.mitrais.rms.validators;

import com.mitrais.rms.domains.User;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {

    private final String REQUIRED = "required";

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        String username = user.getUsername();


        if(username.isEmpty()) {
            errors.rejectValue("username", REQUIRED, REQUIRED);
        }

        if(user.getEmail().isEmpty()) {
            errors.rejectValue("email", REQUIRED, REQUIRED);
        }
    }
}

package com.github.mtahasahin.evently.validator;

import com.ibm.icu.util.ULocale;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;


public class LanguageValidator implements ConstraintValidator<Language, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.asList(ULocale.getISOLanguages()).contains(value);
    }
}

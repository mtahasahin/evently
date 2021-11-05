package com.github.mtahasahin.evently.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Locale;


public class LanguageValidator implements ConstraintValidator<Language, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null)
            return false;

        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            if (value.equals(locale.toString())) {
                return true;
            }
        }
        return false;
    }
}

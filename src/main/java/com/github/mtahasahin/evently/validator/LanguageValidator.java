package com.github.mtahasahin.evently.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Locale;


public class LanguageValidator implements ConstraintValidator<Language, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.equals("")) {
            return true;
        }

        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            if (value.equals(locale.toLanguageTag())) {
                return true;
            }
        }
        return false;
    }
}

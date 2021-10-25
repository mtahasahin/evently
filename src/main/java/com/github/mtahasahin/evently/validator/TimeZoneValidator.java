package com.github.mtahasahin.evently.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.ZoneId;


public class TimeZoneValidator implements ConstraintValidator<TimeZone, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null){
            return false;
        }

        try {
            var zone = ZoneId.of(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

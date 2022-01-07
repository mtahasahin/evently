package com.github.mtahasahin.evently.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, METHOD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = TimeZoneValidator.class)
@Documented
public @interface TimeZone {

    String message() default "invalid timezone";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

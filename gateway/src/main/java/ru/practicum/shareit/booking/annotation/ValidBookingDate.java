package ru.practicum.shareit.booking.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {BookingDateValidator.class})
public @interface ValidBookingDate {

    String message() default "Incorrect booking dates";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

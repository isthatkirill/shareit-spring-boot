package ru.practicum.shareit.booking.annotation;

import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class BookingDateValidator implements ConstraintValidator<ValidBookingDate, BookingDto> {

    @Override
    public void initialize(ValidBookingDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(BookingDto bookingDto, ConstraintValidatorContext context) {
        if (bookingDto.getStart() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Start of booking cannot be null")
                    .addPropertyNode("start")
                    .addConstraintViolation();
            return false;
        } else if (bookingDto.getEnd() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("End of booking cannot be null")
                    .addPropertyNode("end")
                    .addConstraintViolation();
            return false;
        } else if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Start of booking cannot be in past")
                    .addPropertyNode("start")
                    .addConstraintViolation();
            return false;
        } else if (bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("End of booking cannot be in past")
                    .addPropertyNode("end")
                    .addConstraintViolation();
            return false;
        } else if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("End of booking cannot be earlier than start")
                    .addPropertyNode("end")
                    .addConstraintViolation();
            return false;
        } else if (bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("End of booking cannot equals start")
                    .addPropertyNode("end")
                    .addConstraintViolation();
            return false;
        } else {
            return true;
        }
    }
}

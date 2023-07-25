package isthatkirill.shareit.booking.annotation;

import isthatkirill.shareit.booking.dto.BookingDtoRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class BookingDateValidator implements ConstraintValidator<ValidBookingDate, BookingDtoRequest> {

    @Override
    public void initialize(ValidBookingDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(BookingDtoRequest bookingDtoRequest, ConstraintValidatorContext context) {
        if (bookingDtoRequest.getStart() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Start of booking cannot be null")
                    .addPropertyNode("start")
                    .addConstraintViolation();
            return false;
        } else if (bookingDtoRequest.getEnd() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("End of booking cannot be null")
                    .addPropertyNode("end")
                    .addConstraintViolation();
            return false;
        } else if (bookingDtoRequest.getStart().isBefore(LocalDateTime.now())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Start of booking cannot be in past")
                    .addPropertyNode("start")
                    .addConstraintViolation();
            return false;
        } else if (bookingDtoRequest.getEnd().isBefore(LocalDateTime.now())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("End of booking cannot be in past")
                    .addPropertyNode("end")
                    .addConstraintViolation();
            return false;
        } else if (bookingDtoRequest.getStart().isAfter(bookingDtoRequest.getEnd())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("End of booking cannot be earlier than start")
                    .addPropertyNode("end")
                    .addConstraintViolation();
            return false;
        } else if (bookingDtoRequest.getStart().isEqual(bookingDtoRequest.getEnd())) {
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

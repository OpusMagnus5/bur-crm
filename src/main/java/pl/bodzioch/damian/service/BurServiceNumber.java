package pl.bodzioch.damian.service;

import org.springframework.http.HttpStatus;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public record BurServiceNumber(
        String number
) {

    public Long getBurServiceId() {
        try {
            return Optional.ofNullable(number)
                    .map(number -> number.split("/"))
                    .map(Arrays::asList)
                    .map(List::getLast)
                    .map(Long::parseLong)
                    .orElseThrow(this::buildIncorrectServiceNumberException);
        } catch (NumberFormatException | NoSuchElementException e) {
            throw buildIncorrectServiceNumberException();
        }
    }

    private AppException buildIncorrectServiceNumberException() {
        return new AppException(
                "Incorrect service number: " + this.number,
                HttpStatus.BAD_REQUEST,
                List.of(new ErrorData("error.client.service.incorrectServiceNumber", List.of(number)))
        );
    }
}

package edu.java.bot.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import edu.java.bot.controller.dto.response.ApiErrorResponse;
import java.util.Arrays;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ApiErrorResponse> invalidFormatException(InvalidFormatException exception) {
        return ResponseEntity
            .status(400)
            .body(new ApiErrorResponse(
                "Cannot deserialize request body json format",
                "400",
                "Invalid json format",
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList()
            ));
    }

}

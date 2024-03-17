package edu.java.scrapper.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import edu.java.scrapper.controller.dto.response.ApiErrorResponse;
import java.util.Arrays;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class ExceptionApiHandler {
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ApiErrorResponse> invalidFormatException(InvalidFormatException exception) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ApiErrorResponse(
                "Cannot deserialize request body json format",
                HttpStatus.BAD_REQUEST.toString(),
                "Invalid json format",
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList()
            ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> exceptionHandle(Exception exception) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ApiErrorResponse(
                "Some exception during request",
                HttpStatus.BAD_REQUEST.toString(),
                exception.getClass().toString(),
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList()
            ));
    }

}

package edu.java.scrapper.controller;

import edu.java.scrapper.controller.dto.response.ApiErrorResponse;
import edu.java.scrapper.exception.AlreadySubscribedException;
import edu.java.scrapper.exception.NoSuchChatException;
import edu.java.scrapper.exception.NoSuchLinkException;
import edu.java.scrapper.exception.UserAlreadyRegisteredException;
import java.util.Arrays;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SqlExceptionApiHandler {
    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<ApiErrorResponse> duplicateKey(UserAlreadyRegisteredException exception) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ApiErrorResponse(
                "Chat already registered exception",
                HttpStatus.CONFLICT.toString(),
                exception.getClass().toString(),
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList()
            ));
    }

    @ExceptionHandler(NoSuchChatException.class)
    public ResponseEntity<ApiErrorResponse> noSuchChat(NoSuchChatException exception) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new ApiErrorResponse(
                "No such chat exception",
                HttpStatus.UNAUTHORIZED.toString(),
                exception.getClass().toString(),
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList()
            ));
    }

    @ExceptionHandler(AlreadySubscribedException.class)
    public ResponseEntity<ApiErrorResponse> alreadySubscribed(AlreadySubscribedException exception) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ApiErrorResponse(
                "Already subscribed exception",
                HttpStatus.CONFLICT.toString(),
                exception.getClass().toString(),
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList()
            ));
    }

    @ExceptionHandler(NoSuchLinkException.class)
    public ResponseEntity<ApiErrorResponse> alreadySubscribed(NoSuchLinkException exception) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ApiErrorResponse(
                "No such link exception",
                HttpStatus.NOT_FOUND.toString(),
                exception.getClass().toString(),
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList()
            ));
    }

}

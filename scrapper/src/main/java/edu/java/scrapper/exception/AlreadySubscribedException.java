package edu.java.scrapper.exception;

public class AlreadySubscribedException extends RuntimeException {
    public AlreadySubscribedException(String message) {
        super(message);
    }

    public AlreadySubscribedException(String message, Throwable cause) {
        super(message, cause);
    }
}

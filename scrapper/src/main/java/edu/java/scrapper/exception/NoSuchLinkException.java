package edu.java.scrapper.exception;

public class NoSuchLinkException extends RuntimeException {
    public NoSuchLinkException(String message) {
        super(message);
    }

    public NoSuchLinkException(String message, Throwable cause) {
        super(message, cause);
    }
}

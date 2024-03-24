package edu.java.scrapper.exception;

public class NoSuchChatException extends RuntimeException {
    public NoSuchChatException(String message, Throwable cause) {
        super(message, cause);
    }
}

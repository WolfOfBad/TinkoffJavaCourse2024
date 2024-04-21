package edu.java.bot.exception.scrapper;

public abstract class ScrapperException extends RuntimeException {
    public abstract String getTelegramMessage();
}

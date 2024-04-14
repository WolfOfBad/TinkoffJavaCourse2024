package edu.java.bot.exception.scrapper;

public class NoSuchLinkException extends ScrapperException {
    @Override
    public String getTelegramMessage() {
        return "Вы не отслеживали эту ссылку. Введите /list, чтобы увидеть все отслеживаемые ссылки";
    }
}

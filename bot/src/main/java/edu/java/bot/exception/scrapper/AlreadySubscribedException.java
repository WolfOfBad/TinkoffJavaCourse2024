package edu.java.bot.exception.scrapper;

public class AlreadySubscribedException extends ScrapperException {
    @Override
    public String getTelegramMessage() {
        return "Вы уже отслеживаете эту ссылку. Введите /list, чтобы увидеть все отслеживаемые ссылки";
    }
}

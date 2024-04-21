package edu.java.bot.exception.scrapper;

public class NoSuchChatException extends ScrapperException {
    @Override
    public String getTelegramMessage() {
        return "Вы не зарегестрированны в боте. Напишите /start, чтобы начать работу с ботом";
    }
}

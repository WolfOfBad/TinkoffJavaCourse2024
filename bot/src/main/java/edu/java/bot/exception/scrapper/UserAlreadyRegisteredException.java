package edu.java.bot.exception.scrapper;

public class UserAlreadyRegisteredException extends ScrapperException {
    @Override
    public String getTelegramMessage() {
        return "Вы уже зарегестрированы в боте. Чтобы сбросить ссылки отправьте команду /reset";
    }
}

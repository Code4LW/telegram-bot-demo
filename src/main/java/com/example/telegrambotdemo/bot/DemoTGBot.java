package com.example.telegrambotdemo.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DemoTGBot extends TelegramLongPollingBot {

    public DemoTGBot(@Value("${bot.token}") String token){
        super(token);
    }


    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return "DemoTGBot";
    }
}

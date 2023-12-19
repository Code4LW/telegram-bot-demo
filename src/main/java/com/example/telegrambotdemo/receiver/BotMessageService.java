package com.example.telegrambotdemo.receiver;

import com.example.telegrambotdemo.bot.DemoTGBot;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Data
public class BotMessageService {


    private final ElementService elementService;

    private final DemoTGBot demoTGBot;

    @Autowired
    public BotMessageService(DemoTGBot demoTGBot, ElementService elementService) {
        this.demoTGBot = demoTGBot;
        this.elementService = elementService;
    }
//Реализация логики отправки сообщений
    public void sendMessage(Long chatId, String text) {
        String chatIdStr = String.valueOf(chatId);
        SendMessage sendMessage = new SendMessage(chatIdStr, text);
        try {
            demoTGBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void sendDocument(SendDocument sendDocument){
        try {
            demoTGBot.execute(sendDocument);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public File getFile(GetFile getFile){
        try {
            return demoTGBot.execute(getFile);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

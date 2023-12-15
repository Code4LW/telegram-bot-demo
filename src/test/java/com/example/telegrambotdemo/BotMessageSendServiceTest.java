package com.example.telegrambotdemo;

import ch.qos.logback.classic.encoder.JsonEncoder;
import com.example.telegrambotdemo.bot.DemoTGBot;
import com.example.telegrambotdemo.receiver.BotMessageService;
import com.example.telegrambotdemo.receiver.ElementService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@SpringBootTest
public class BotMessageSendServiceTest {

    private BotMessageService botMessageService;
    private DemoTGBot demoTGBot;

    @Autowired
    private ElementService elementService;

    @BeforeEach
    public void init(){
        demoTGBot = Mockito.mock(DemoTGBot.class);
        botMessageService = new BotMessageService(demoTGBot,elementService);
    }
    @Test
    public void sendMessageTest() throws TelegramApiException{
        String chatId = "1";
        String message = "test_message";

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(message);
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        Long id = Long.parseLong(chatId);

        ArgumentCaptor<SendMessage> capturedArg = ArgumentCaptor.forClass(SendMessage.class);


        botMessageService.sendMessage(id, message);
        //capturing the SendMessage object demoTgBot sends
        Mockito.verify(demoTGBot).execute(capturedArg.capture());
        SendMessage capturedMessage = capturedArg.getValue();
        //Setting the parsemode of captured message to "null"
        capturedMessage.setParseMode("null");
        Mockito.verify(demoTGBot).execute(capturedMessage);




    }
}

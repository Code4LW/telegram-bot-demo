package com.example.telegrambotdemo.command;

import com.example.telegrambotdemo.receiver.BotMessageService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Component
public class StartCommand implements BotCommand{

    private final BotMessageService botMessageService;
    private static final String MESSAGE = """
            Hi, %s!
            How can I help you?
            """;

    @Override
    public void execute(Update update){
        String message = String.format(MESSAGE, update.getMessage().getChat().getUserName());
        botMessageService.sendMessage(update.getMessage().getChatId(), message);
    }
}

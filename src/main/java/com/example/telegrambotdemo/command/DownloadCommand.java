package com.example.telegrambotdemo.command;

import com.example.telegrambotdemo.receiver.BotMessageService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class DownloadCommand implements BotCommand{

    public final BotMessageService botMessageService;

    private static final String MESSAGE = "http://localhost:8080/download";
    @Override
    public void execute(Update update) {
        botMessageService.sendMessage(update.getMessage().getChatId(), MESSAGE);
    }
}

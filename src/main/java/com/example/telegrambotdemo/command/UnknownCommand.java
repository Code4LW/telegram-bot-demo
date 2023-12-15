package com.example.telegrambotdemo.command;

import com.example.telegrambotdemo.receiver.BotMessageService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class UnknownCommand implements BotCommand{
    private final BotMessageService botMessageService;
    private static final String MESSAGE = "Sorry, I don't understand this command. Try /help to see the list of available commands.";
    @Override
    public void execute(Update update) {
        botMessageService.sendMessage(update.getMessage().getChatId(), MESSAGE );
    }
}

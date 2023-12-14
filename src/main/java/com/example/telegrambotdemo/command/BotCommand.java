package com.example.telegrambotdemo.command;

import com.example.telegrambotdemo.receiver.BotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

@FunctionalInterface
public interface BotCommand {
      void execute(Update update);
}

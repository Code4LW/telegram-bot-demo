package com.example.telegrambotdemo.bot;

import com.example.telegrambotdemo.command.BotCommand;
import com.example.telegrambotdemo.command.CommandContainer;
import com.example.telegrambotdemo.command.StartCommand;
import com.example.telegrambotdemo.receiver.BotMessageService;
import com.example.telegrambotdemo.receiver.ElementService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class DemoTGBot extends TelegramLongPollingBot {

//    @Value("${bot.name}")
    private final String botName = "DemoBot";

//    @Value("${bot.token}")
    private String token;
    private final CommandContainer commandContainer;
    private final ElementService elementService;
    public DemoTGBot(@Value("${bot.token}") String token, ElementService elementService){
        super(token);
        this.elementService = elementService;
        this.commandContainer = new CommandContainer(new BotMessageService(this, elementService));
    }
    @Override
    public void onUpdateReceived(Update update) {
        if(!update.hasMessage()||!update.getMessage().hasText()) return;
        String message = update.getMessage().getText().split(" ")[0];
        if(commandContainer.getCommands().containsKey(message)){
            commandContainer.getCommands().get(message).execute(update);
        }
    }

    @Override
    public String getBotUsername() {
        return "PDevDemoBot";
    }

}

package com.example.telegrambotdemo.command;

import com.example.telegrambotdemo.model.Element;
import com.example.telegrambotdemo.receiver.BotMessageService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class RemoveCommand implements BotCommand{

    private final BotMessageService botMessageService;

    @Override
    public void execute(Update update) {
        String message = update.getMessage().getText().replace("/removeElement","").trim();
        Element element = botMessageService.getElementService().getByName(message);
        if(!botMessageService.getElementService().deleteElement(element)){
            botMessageService.sendMessage(update.getMessage().getChatId(), String.format("There is no element by the name %s", message));
            return;
        }
        botMessageService.sendMessage(update.getMessage().getChatId(), String.format("The element %s has been removed from the tree", message));
    }
}

package com.example.telegrambotdemo.command;

import com.example.telegrambotdemo.model.Element;
import com.example.telegrambotdemo.receiver.BotMessageService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class AddElementCommand implements BotCommand{
    private final BotMessageService botMessageService;
    @Override
    public void execute(Update update){
        String message = update.getMessage().getText().replace("/addElement", "");
        if(message.isEmpty()) {
            botMessageService.sendMessage(update.getMessage().getChatId(), "No element is given");
            return;
        }
        message = message.trim();
        if(!message.contains(",")){
            Element element = new Element();
            element.setName(message);
            if(botMessageService.getElementService().addElement(element)){
                    botMessageService.sendMessage(update.getMessage().getChatId(), "A new element is added");
                    return;
            }
            botMessageService.sendMessage(update.getMessage().getChatId(), "Sorry such element already exists");
        }else{
            String parentName = message.split(",")[0].trim();
            if(!botMessageService.getElementService().existByName(parentName)){
                botMessageService.sendMessage(update.getMessage().getChatId(), "The parent element does not exist. Please make sure you typed the name right");
                return;
            }
            String childName = message.split(",")[1].trim();
            if(botMessageService.getElementService().addElement(parentName, childName)){
                botMessageService.sendMessage(update.getMessage().getChatId(), String.format("The new element is added to the parent element: %s",parentName));
            }else botMessageService.sendMessage(update.getMessage().getChatId(), "Sorry the given child element already exists in the tree. Please give a more distinct name");
        }

    }

}

package com.example.telegrambotdemo.command;

import com.example.telegrambotdemo.receiver.BotMessageService;
import lombok.RequiredArgsConstructor;


import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class ViewTreeCommand implements BotCommand{

    private final BotMessageService botMessageService;
    @Override
    public void execute(Update update) {
        if(botMessageService.getElementService().getAll().isEmpty()||botMessageService.getElementService().getAll()==null){
            botMessageService.sendMessage(update.getMessage().getChatId(), "The tree is empty, you can create it by adding new elements");
        }else {
            String tree = botMessageService.getElementService().drawTree();
            botMessageService.sendMessage(update.getMessage().getChatId(), "<code>" +tree+"</code>");
        }
    }

}

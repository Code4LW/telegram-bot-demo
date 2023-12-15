package com.example.telegrambotdemo.command;

import com.example.telegrambotdemo.receiver.BotMessageService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class HelpCommand implements BotCommand{

    private final BotMessageService botMessageService;

    private static final String MESSAGE =
            """
            Available commands:
                /viewTree - to get a tree of your elements
                /addElement <element name> - to add a new element. If there is no elements, the added element will become root
                /addElement <parent name> <child name> - to add a new  child element to a specific element
                /removeElement - to remove an element from the tree  
                /download - to download your tree as an excel file
            """;
    @Override
    public void execute(Update update){
        botMessageService.sendMessage(update.getMessage().getChatId(), MESSAGE);
    }
}

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
                /addElement &lt;element name&gt; - to add a new element. If there are no elements, the added element will become the root
                /addElement &lt;parent name&gt;, &lt;child name&gt; - to add a new child element to a specific element
                /removeElement - to remove an element from the tree
                /download - to download your tree as an Excel file
                /upload - to upload an Excel file to convert into a tree
            """;
    @Override
    public void execute(Update update){
        botMessageService.sendMessage(update.getMessage().getChatId(), MESSAGE);
    }
}

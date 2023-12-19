package com.example.telegrambotdemo.command;

import com.example.telegrambotdemo.receiver.BotMessageService;

import java.util.HashMap;
import java.util.Map;

public class CommandContainer {

    private final Map<String, BotCommand> map = new HashMap<>();

    public CommandContainer(BotMessageService botMessageService){
        this.map.put("/start", new StartCommand(botMessageService));
        this.map.put("/viewTree", new ViewTreeCommand(botMessageService));
        this.map.put("/addElement", new AddElementCommand(botMessageService));
        this.map.put("/help", new HelpCommand(botMessageService));
        this.map.put("/removeElement", new RemoveCommand(botMessageService));
        this.map.put("/download", new DownloadCommand(botMessageService));
        this.map.put("/upload", new UploadCommand(botMessageService));
    }

    public Map<String, BotCommand> getCommands(){
        return this.map;
    }
}

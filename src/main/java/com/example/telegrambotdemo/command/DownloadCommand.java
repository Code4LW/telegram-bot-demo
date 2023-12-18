package com.example.telegrambotdemo.command;

import com.example.telegrambotdemo.receiver.BotMessageService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.*;

@RequiredArgsConstructor
public class DownloadCommand implements BotCommand{

    public final BotMessageService botMessageService;

    @Override
    public void execute(Update update) {
        try {
            SendDocument document = new SendDocument();
            document.setChatId(update.getMessage().getChatId());

            File file = new File("elemet_tree.xlsx");
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(botMessageService.getElementService().getExcel());
            InputFile inputFile = new InputFile(file);
            document.setDocument(inputFile);
            document.setCaption("Here is your file");
            botMessageService.sendDocument(document);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

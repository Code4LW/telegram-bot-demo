package com.example.telegrambotdemo.command;

import com.example.telegrambotdemo.bot.DemoTGBot;
import com.example.telegrambotdemo.receiver.BotMessageService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.*;
import java.net.URL;

@RequiredArgsConstructor
public class UploadCommand implements BotCommand {

    private final BotMessageService botMessageService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadCommand.class);

    @Override
    public void execute(Update update) {
        LOGGER.info("method is called" );
        if(!update.getMessage().hasDocument()){
            botMessageService.sendMessage(update.getMessage().getChatId(), "Please upload an excel file and try again");
            return;
        }
        System.out.println("checking the document");
        final Document document = update.getMessage().getDocument();
        if (!document.getFileName().contains(".xlsx")) {
            botMessageService.sendMessage(update.getMessage().getChatId(), "Sorry but I can only accept an excel file");
            return;
        }
        LOGGER.info("started parsing the document"+update.getMessage().getCaption());
        InputFile inputFile = new InputFile();
        GetFile getFile = new GetFile(document.getFileId());
        File file = botMessageService.getFile(getFile);
        String token = botMessageService.getDemoTGBot().getToken();
        downloadFile(file.getFileUrl(token), document.getFileName());
        try {
            FileInputStream fileInputStream = new FileInputStream(document.getFileName());
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            botMessageService.getElementService().getFromExcel(workbook);
        } catch (IOException e) {
            e.printStackTrace();
        }
        botMessageService.sendMessage(update.getMessage().getChatId(), "you tree is ready");
    }

    private void downloadFile(String fileURL, String fileName) {
        InputStream inputStream = null;
        try {
            inputStream = new URL(fileURL).openStream();

            FileOutputStream fileOutputStream = new FileOutputStream(fileName);

            byte[] bytes = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

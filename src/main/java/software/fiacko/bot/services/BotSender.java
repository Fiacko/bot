package software.fiacko.bot.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaDocument;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import software.fiacko.bot.config.TelegramProperties;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BotSender {
    private final Bot bot;
    private final TelegramProperties properties;

    public void sendFile(File file) {
        InputFile inputFile = createInputFile(file);
        SendDocument sendDocumentRequest = new SendDocument();
        sendDocumentRequest.setChatId(properties.getChatId());
        sendDocumentRequest.setCaption("Сформирован файл:");
        sendDocumentRequest.setDocument(inputFile);
        try {
            bot.execute(sendDocumentRequest);
        }
        catch (TelegramApiException e) {
            throw new RuntimeException("Error on sending file ", e);
        }
    }

    public void sendFiles(List<File> files) {
        try {
            sendFilesInternal(files);
        }
        catch (TelegramApiException e) {
            throw new RuntimeException("Error on sending file ", e);
        }
    }

    private void sendFilesInternal(List<File> files) throws TelegramApiException {
        List<InputMedia> sublist = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            sublist.add(createMediaFile(files.get(i)));

            if (i > 0 && i % 10 == 0) {
                SendMediaGroup sendGroupRequest = new SendMediaGroup(properties.getChatId(), sublist);
                bot.execute(sendGroupRequest);
                sublist.clear();
            }
        }
        if (sublist.size() > 0) {
            SendMediaGroup sendGroupRequest = new SendMediaGroup(properties.getChatId(), sublist);
            bot.execute(sendGroupRequest);
        }
    }

    public InputMedia createMediaFile(File file) {
        InputMediaDocument inputMedia = new InputMediaDocument();
        inputMedia.setMedia(file, file.getName());
        return inputMedia;
    }

    public InputFile createInputFile(File file) {
        InputFile inputFile = new InputFile();
        inputFile.setMedia(file);
        return inputFile;
    }
}

package software.fiacko.bot.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import software.fiacko.bot.config.TelegramProperties;

import java.io.File;

@RequiredArgsConstructor
@Service
public class BotSender {
    private final Bot bot;
    private final TelegramProperties properties;

    public void sendFile(InputFile file) {
        SendDocument sendDocumentRequest = new SendDocument();
        sendDocumentRequest.setChatId(properties.getChatId());
        sendDocumentRequest.setCaption("Сформирован файл:");
        sendDocumentRequest.setDocument(file);
        try {
            bot.execute(sendDocumentRequest);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Error on sending file ", e);
        }
    }

    public InputFile createInputFile(File file) {
        InputFile inputFile = new InputFile();
        inputFile.setMedia(file);
        return inputFile;
    }
}

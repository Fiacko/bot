package software.fiacko.bot.services;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import software.fiacko.bot.config.FileCheckerProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class FileScheduler {
    private final BotSender sender;
    private final FileCheckerProperties properties;

    private Instant lastSendingTime = Instant.now();

    @Scheduled(fixedDelayString = "${software.fiacko.bot.file-checker.delay}")
    protected void checkFiles() {
        try (Stream<Path> paths = Files.walk(Paths.get(properties.getDirectory()))) {
            List<Path> files = paths
                    .filter(this::filterFile)
                    .toList();
            files.forEach(file -> {
                InputFile inputFile = sender.createInputFile(file.toFile());
                sender.sendFile(inputFile);
            });
            if (!files.isEmpty()) {
                lastSendingTime = Instant.now();
            }

        } catch (IOException e) {
            throw new RuntimeException("Error on getting file list: ", e);
        }
    }

    private boolean filterFile(Path file) {
        BasicFileAttributes attributes = readAttributes(file);

        return Files.isRegularFile(file) &&
                attributes.size() > 0 &&
                lastSendingTime.isBefore(attributes.creationTime().toInstant());
    }

    private BasicFileAttributes readAttributes(Path path) {
        try {
            return Files.readAttributes(path, BasicFileAttributes.class);
        } catch (IOException e) {
            throw new RuntimeException("Error on reading file attributes: ", e);
        }
    }

}

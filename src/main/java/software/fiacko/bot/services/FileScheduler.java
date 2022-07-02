package software.fiacko.bot.services;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.fiacko.bot.config.FileCheckerProperties;

import java.io.File;
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
        List<File> files;
        try (Stream<Path> paths = Files.walk(Paths.get(properties.getDirectory()))) {
            files = paths
                    .filter(this::filterFile)
                    .map(Path::toFile)
                    .toList();
        }
        catch (IOException e) {
            throw new RuntimeException("Error on getting file list: ", e);
        }

        if (files.isEmpty()) {
            return;
        }

        if (files.size() > 1) {
            sender.sendFiles(files);
        }
        else {
            sender.sendFile(files.get(0));
        }

        lastSendingTime = Instant.now();
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
        }
        catch (IOException e) {
            throw new RuntimeException("Error on reading file attributes: ", e);
        }
    }

}

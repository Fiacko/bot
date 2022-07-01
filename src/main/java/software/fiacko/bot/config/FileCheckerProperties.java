package software.fiacko.bot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("software.fiacko.bot.file-checker")
public class FileCheckerProperties {
    private String directory;
}

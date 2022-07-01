package software.fiacko.bot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("software.fiacko.bot.telegram")
public class TelegramProperties {
    private String botToken;
    private String chatId;
    private String botName;
}

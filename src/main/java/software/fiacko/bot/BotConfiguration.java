package software.fiacko.bot;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@PropertySource("classpath:bot.properties")
@ConfigurationPropertiesScan
@EnableScheduling
@Configuration
public class BotConfiguration {

    @Bean
    protected TelegramBotsApi getApi() throws TelegramApiException {
        return new TelegramBotsApi(DefaultBotSession.class);
    }
}

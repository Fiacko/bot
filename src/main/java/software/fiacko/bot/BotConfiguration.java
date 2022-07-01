package software.fiacko.bot;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@PropertySource("classpath:bot.properties")
@ConfigurationPropertiesScan
@EnableScheduling
@Configuration
public class BotConfiguration {
}

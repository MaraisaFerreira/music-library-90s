package maraisaferreira.com.github.music_library_90s.config;

import org.springframework.boot.flyway.autoconfigure.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DatabaseDevConfig {

    @Bean
    FlywayMigrationStrategy cleanDevDatabase(){
        return flyway -> {
          flyway.clean();
          flyway.migrate();
        };
    }
}

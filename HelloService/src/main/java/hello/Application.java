package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@SpringBootApplication
public class Application {

    @Bean
    public EmbeddedDatabase embeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
                .continueOnError(true)
                .setType(EmbeddedDatabaseType.H2)
                .ignoreFailedDrops(true)
                .addDefaultScripts()
            .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

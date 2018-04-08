package audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class Application {

    @Component
    public static class CreateScript implements CommandLineRunner {

        @Autowired private ScriptRepository scriptRepository;

        @Override
        public void run(String... args) throws Exception {
            Script script = new Script();
            script.setText("The script in all its original glory.");
            scriptRepository.save(script);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
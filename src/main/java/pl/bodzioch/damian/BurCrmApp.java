package pl.bodzioch.damian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
class BurCrmApp {

    public static void main(String[] args) {
        SpringApplication.run(BurCrmApp.class, args);
    }
}

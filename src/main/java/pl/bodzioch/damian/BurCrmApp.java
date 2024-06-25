package pl.bodzioch.damian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
class BurCrmApp {

    public static void main(String[] args) {
        SpringApplication.run(BurCrmApp.class, args);
    }
}

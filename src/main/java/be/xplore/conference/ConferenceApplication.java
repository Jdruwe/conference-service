package be.xplore.conference;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@SpringBootApplication
public class ConferenceApplication {

    @PostConstruct
    public void init(){
        System.out.println("Spring boot application running in UTC timezone :" + LocalDateTime.now());   // It will print UTC timezone
    }

    public static void main(String[] args) {
        SpringApplication.run(ConferenceApplication.class, args);
    }
}

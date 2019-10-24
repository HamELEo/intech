package com.intech;

import com.intech.repository.ContentRepository;
import com.intech.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

@EntityScan("com.intech")
@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(ContentRepository repository, UserRepository userRepository) {
        return (args -> {
            repository.save(new Content("Вести"));
            repository.save(new Content("Время"));
            repository.save(new Content("Мстители"));
            userRepository.save(new User("Иван"));
            userRepository.save(new User("Мария"));
        });
    }

}

package com.interiorai;

import com.interiorai.entity.User;
import com.interiorai.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class InteriorAiApplication {

    private static final Logger log = LoggerFactory.getLogger(InteriorAiApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(InteriorAiApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDemoUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            try {
                String demoEmail = "demo@interiorai.com";
                User demoUser = userRepository.findByEmail(demoEmail).orElse(null);
                if (demoUser == null) {
                    demoUser = new User("Aarav Sharma", demoEmail, passwordEncoder.encode("password123"), "+91 98765 43210");
                    demoUser.setRole("ROLE_USER");
                    userRepository.save(demoUser);
                    log.info("Initialized default demo user: {}", demoEmail);
                } else {
                    demoUser.setPassword(passwordEncoder.encode("password123"));
                    userRepository.save(demoUser);
                    log.info("Synchronized password for demo user: {}", demoEmail);
                }
            } catch (Exception ex) {
                log.warn("Demo user initialization notice: {}", ex.getMessage());
            }
        };
    }
}

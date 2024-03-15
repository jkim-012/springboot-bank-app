package com.cos.bank.config.dummy;

import com.cos.bank.user.domain.User;
import com.cos.bank.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DummyDevInit extends DummyObject{

    // when running on dev environment, user will be created automatically
    @Profile("dev")
    @Bean
    CommandLineRunner init(UserRepository userRepository){
       return (args -> {
           User user = userRepository.save(newUser("ssar", "first", "last"));
       });
    }
}

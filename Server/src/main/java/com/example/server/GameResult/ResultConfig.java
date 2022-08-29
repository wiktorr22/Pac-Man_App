package com.example.server.GameResult;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class ResultConfig {

   // @Bean
    CommandLineRunner commandLineRunner(ResultRepository resultRepository) {
        return args -> {
            GameResult firstGame =
                    new GameResult(LocalDate.of(2022, Month.AUGUST, 22), 400, 3);
            GameResult secondGame =
                    new GameResult(LocalDate.of(2022, Month.AUGUST, 22), 200, 2);


            resultRepository.saveAll(List.of(firstGame, secondGame));
        };
    }
}

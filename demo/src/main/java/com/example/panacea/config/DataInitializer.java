package com.example.panacea.config;

import com.example.panacea.enums.Belt;
import com.example.panacea.enums.Gender;
import com.example.panacea.models.Program;
import com.example.panacea.repo.ProgramRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner seedPrograms(ProgramRepository programRepository) {
        return args -> {
            // Use args to avoid unused-parameter warnings
            if (args != null && args.length > 0) {
                log.debug("DataInitializer received {} args", args.length);
            }
            if (programRepository.count() > 0) {
                return;
            }
            log.info("Seeding demo programs...");

            Program beginnerKids = Program.builder()
                    .name("Beginner Kids")
                    .description("Introductory karate for ages 6-10")
                    .rate(new BigDecimal("79.99"))
                    .durationMinutes(60)
                    .genderReq(null)
                    .minAge(6)
                    .minBelt(Belt.WHITE)
                    .minYearsInClub(0)
                    .capacity(20)
                    .daysOfWeek("MON,WED")
                    .build();

            Program teenIntermediate = Program.builder()
                    .name("Teen Intermediate")
                    .description("Skill building for teens with prior experience")
                    .rate(new BigDecimal("99.99"))
                    .durationMinutes(75)
                    .genderReq(null)
                    .minAge(12)
                    .minBelt(Belt.ORANGE)
                    .minYearsInClub(1)
                    .capacity(18)
                    .daysOfWeek("TUE,THU")
                    .build();

            Program womenSelfDefense = Program.builder()
                    .name("Women Self-Defense")
                    .description("Focused self-defense program for women")
                    .rate(new BigDecimal("89.99"))
                    .durationMinutes(60)
                    .genderReq(Gender.FEMALE)
                    .minAge(16)
                    .minBelt(Belt.WHITE)
                    .minYearsInClub(0)
                    .capacity(15)
                    .daysOfWeek("SAT")
                    .build();

            Program advancedBlackBelt = Program.builder()
                    .name("Advanced Black Belt")
                    .description("High-intensity training for black belts")
                    .rate(new BigDecimal("129.99"))
                    .durationMinutes(90)
                    .genderReq(null)
                    .minAge(16)
                    .minBelt(Belt.BLACK)
                    .minYearsInClub(3)
                    .capacity(12)
                    .daysOfWeek("FRI")
                    .build();

            programRepository.saveAll(List.of(beginnerKids, teenIntermediate, womenSelfDefense, advancedBlackBelt));
            log.info("Seeded {} programs", programRepository.count());
        };
    }
}

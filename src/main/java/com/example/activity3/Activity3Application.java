package com.example.activity3;

import com.example.activity3.Entities.Patient;
import com.example.activity3.Repositories.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.Date;

@SpringBootApplication
public class Activity3Application {

    public static void main(String[] args) {
        SpringApplication.run(Activity3Application.class, args);
    }
    @Bean
    CommandLineRunner start(PatientRepository patientRepository) {
        return args -> {
            patientRepository.save(new Patient(null, "Mohamed", new Date(), false, 34));
            patientRepository.save(new Patient(null, "Hanane", new Date(), false, 4321));
            patientRepository.save(new Patient(null, "Imane", new Date(), true, 34));
        };
    }

    //@Bean
    CommandLineRunner commandLineRunner(JdbcUserDetailsManager jdbcUserDetailsManager){
        return args -> {
            jdbcUserDetailsManager.createUser(User.withUsername("user11")
                    .password(passwordEncoder().encode("1234"))
                    .roles("USER")
                    .build());
            jdbcUserDetailsManager.createUser(User.withUsername("user22")
                    .password(passwordEncoder().encode("1234"))
                    .roles("USER")
                    .build());
            jdbcUserDetailsManager.createUser(User.withUsername("admin2")
                    .password(passwordEncoder().encode("1234"))
                    .roles("ADMIN","USER")
                    .build());
        };
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

package com.example.activity3;

import com.example.activity3.Entities.Patient;
import com.example.activity3.Repositories.PatientRepository;
import com.example.activity3.Security.Service.AccountService;
import com.example.activity3.Security.entities.AppRole;
import com.example.activity3.Security.entities.AppUser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class Activity3Application {

    public static void main(String[] args) {
        SpringApplication.run(Activity3Application.class, args);
    }
    //@Bean
    CommandLineRunner start(PatientRepository patientRepository) {
        return args -> {
            patientRepository.save(new Patient(null, "Mohamed", new Date(), false, 34));
            patientRepository.save(new Patient(null, "Hanane", new Date(), false, 4321));
            patientRepository.save(new Patient(null, "Imane", new Date(), true, 34));
        };
    }
    //@Bean
    CommandLineRunner commandLineRunnerUserDetails(AccountService accountService) {
        return args -> {
            AppRole appRoleUser = new AppRole("us_role", "ROLE_USER");
            AppRole appRoleAdmin = new AppRole("ad_role", "ROLE_ADMIN");
            accountService.AddNewRole(appRoleUser);
            accountService.AddNewRole(appRoleAdmin);
            List<AppRole> appRolesUser = new ArrayList<>();
            List<AppRole> appRolesAdmin = new ArrayList<>();
            AppUser appUser1 = new AppUser("us_user1", "user1", "1234", "user1@gmail.com", appRolesUser);
            AppUser appAdmin = new AppUser("ad_admin", "admin", "1234", "admin@gmail.com", appRolesAdmin);
            AppUser appUser2 = new AppUser("us_user2", "user2", "1234", "user2@gmail.com", appRolesUser);
            accountService.AddNewUser(appUser1, "1234");
            accountService.AddNewUser(appAdmin, "1234");
            accountService.AddNewUser(appUser2, "1234");
            accountService.AddUserRole(appUser1.getUsername(), appRoleUser.getRoleName());
            accountService.AddUserRole(appUser2.getUsername(), appRoleUser.getRoleName());
            accountService.AddUserRole(appAdmin.getUsername(), appRoleAdmin.getRoleName());
            accountService.AddUserRole(appAdmin.getUsername(), appRoleUser.getRoleName());
        };
    }

    //@Bean
    CommandLineRunner commandLineRunner(JdbcUserDetailsManager jdbcUserDetailsManager){
        return args -> {
            jdbcUserDetailsManager.createUser(User.withUsername("user11")
                    .password(passwordEncoder1().encode("1234"))
                    .roles("USER")
                    .build());
            jdbcUserDetailsManager.createUser(User.withUsername("user22")
                    .password(passwordEncoder1().encode("1234"))
                    .roles("USER")
                    .build());
            jdbcUserDetailsManager.createUser(User.withUsername("admin2")
                    .password(passwordEncoder1().encode("1234"))
                    .roles("ADMIN","USER")
                    .build());
        };
    }
    @Bean
    PasswordEncoder passwordEncoder1() {
        return new BCryptPasswordEncoder();
    }
}

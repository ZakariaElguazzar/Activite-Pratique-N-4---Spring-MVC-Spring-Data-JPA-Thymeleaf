package com.example.activity3.Security;
import com.example.activity3.Security.Service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@AllArgsConstructor
public class SecurityConfig {
    private UserDetailsServiceImpl userDetailsServiceImpl;

     //@Bean
     public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource){
         return new JdbcUserDetailsManager(dataSource);
     }

    //@Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager( PasswordEncoder passwordEncoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername("user1").password(passwordEncoder.encode("1234")).roles("USER").build(),
                User.withUsername("user2").password(passwordEncoder.encode("1234")).roles("USER").build(),
                User.withUsername("admin").password(passwordEncoder.encode("1234")).roles("USER","ADMIN").build()
        );
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .rememberMe(rm->rm
                        .key("uniqueAndSecret")
                        .tokenValiditySeconds(86400)
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(form->form
                        .loginPage("/login")
                        .defaultSuccessUrl("/",true)
                        .permitAll())
//                .authorizeHttpRequests(ar->ar.requestMatchers("/delete/**").hasRole("ADMIN"))
//                .authorizeHttpRequests(ar->ar.requestMatchers("/admin/**").hasRole("ADMIN"))
//                .authorizeHttpRequests(ar->ar.requestMatchers("/user/**").hasRole("USER"))
                .authorizeHttpRequests(ar->ar.requestMatchers("/webjars/**").permitAll())
                .authorizeHttpRequests(ar->ar.requestMatchers("/csrf").permitAll())
                .authorizeHttpRequests(ar->ar.requestMatchers("/addPatient").permitAll())
                .authorizeHttpRequests(ar->ar.anyRequest().authenticated())
                .userDetailsService(userDetailsServiceImpl)
//                .authorizeHttpRequests(ar->ar.anyRequest().permitAll())
                .exceptionHandling(ar->ar.accessDeniedPage("/notAuthorized"));
        return httpSecurity.build();
    }

}

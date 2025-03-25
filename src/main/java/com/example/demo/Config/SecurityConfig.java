package com.example.demo.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/customers/**").authenticated()  // Csak bejelentkezés után érhetők el
                        .anyRequest().permitAll()  // Minden más endpoint szabadon elérhető
                )
                .csrf(csrf -> csrf.disable())
                .formLogin()
                .and()
                .httpBasic();

        return http.build();
    }
}
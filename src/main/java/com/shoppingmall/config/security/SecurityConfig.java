package com.shoppingmall.config.security;


import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Autowired
  private CustomUserDetailsService userDetailsService;


  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
                // 인증 안하고 접근할 수 있는 경로
            .requestMatchers("/", "/home", "/login", "/logout", "/register/**" ,
                    "/index.html" , "/find/**" ).permitAll()
            .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                // 그 외 모든 경로는 인증 필요.
            .anyRequest().authenticated()
        )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login/process")
                .defaultSuccessUrl("/home" , true)
                .permitAll()
            )
        .logout(logout -> logout
            .logoutSuccessUrl("/home")
            .permitAll()
        );


   return http.build();
  }


}

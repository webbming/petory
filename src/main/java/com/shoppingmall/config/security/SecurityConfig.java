package com.shoppingmall.config.security;


import com.shoppingmall.oauth2.CustomSuccessHandler;
import com.shoppingmall.oauth2.service.CustomOAuth2UserService;
import com.shoppingmall.user.jwt.JWTUtil;
import com.shoppingmall.user.jwt.JwtFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private CustomUserDetailsService userDetailsService;
  private CustomAuthenticationFailureHandler failureHandler;
  private CustomOAuth2UserService customOAuth2UserService;
  private final CustomSuccessHandler successHandler;
  private final JWTUtil jwtUtil;

  public SecurityConfig(CustomUserDetailsService userDetailsService,
                        CustomAuthenticationFailureHandler failureHandler,
                        CustomOAuth2UserService customOAuth2UserService,
                        CustomSuccessHandler successHandler,
                        JWTUtil jwtUtil) {
      this.userDetailsService = userDetailsService;
      this.failureHandler = failureHandler;
      this.customOAuth2UserService = customOAuth2UserService;
      this.successHandler = successHandler;
      this.jwtUtil = jwtUtil;
  }


  @Bean
  public HttpFirewall allowSemicolonHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(true); // 세미콜론 허용
        return firewall;
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.httpBasic(auth -> auth.disable());

    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
                // 인증 안하고 접근할 수 있는 경로
                .requestMatchers("/", "/home", "/index.html" , "/find/**" , "/information").permitAll()
                .requestMatchers("/register/**").permitAll()
                .requestMatchers("/login" , "/login/oauth2/**" , "/logout").permitAll()
                .requestMatchers(HttpMethod.POST , "/find/id").permitAll()
                .requestMatchers("/cart/**").permitAll() // 수민님
                .requestMatchers("/product/**").permitAll() // 진호님
                .requestMatchers("/board/**").permitAll() // 준서님
                .requestMatchers("/order/**").permitAll() // 성호님
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                // 그 외 모든 경로는 인증 필요.
                .anyRequest().permitAll())
                //폼 로그인 설정 시작
                .formLogin(form -> form
                    //로그인 페이지는 /login 경로로 설정
                .loginPage("/login")
                    //로그인 요청 경로는 /login/process
                    .loginProcessingUrl("/login/process")
                    // 성공시 /home 으로 리다이렉트
                        .defaultSuccessUrl("/home")
                    // 실패시 실패 핸들러호출
                    .failureHandler(failureHandler))
            // 소셜 로그인 설정 시작
            .oauth2Login(oauth2 -> oauth2
                    // 소셜 로그인 페이지도 /login 경로로 설정
                    .loginPage("/login")
                    // 성공시 /home으로 리다이렉트
                    .defaultSuccessUrl("/home")
                    .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService)))
            .logout(logout -> logout
            .logoutSuccessUrl("/home")
            .permitAll());

    // 세션 생성 x 설정 (jwt 방식 이용)
    http
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                            .maximumSessions(1)
                            .expiredUrl("/login")
                        );


    // 프론트엔드 개발시에 cors 에러 localhost:3000 포트 허용
    http.cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
          CorsConfiguration configuration = new CorsConfiguration();

          configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://mydomain.com"));
          configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
          configuration.setAllowCredentials(true);
          configuration.setAllowedHeaders(Collections.singletonList("*"));
          configuration.setMaxAge(3600L);

          configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));

          return configuration;
    }));
      return http.build();
  }
}

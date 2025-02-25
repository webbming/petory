package com.shoppingmall.user.jwt;

import com.shoppingmall.config.security.CustomUserDetails;
import com.shoppingmall.oauth2.model.CustomOAuth2User;
import com.shoppingmall.user.dto.UserResponseDTO;
import com.shoppingmall.user.model.User;
import com.shoppingmall.user.model.UserRoleType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {


  private final JWTUtil jwtUtil;
  private final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

  public JwtFilter(JWTUtil jwtUtil) {
    this.jwtUtil = jwtUtil;

  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String authorization = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("Authorization")) {
          authorization = cookie.getValue();
        }
      }
    }
    if (authorization == null) {
      logger.debug("token is null");
      filterChain.doFilter(request, response);
      return;
    }

    String token = authorization;

    if (jwtUtil.isExpired(token)) {
      System.out.println("token is expired");
      filterChain.doFilter(request, response);
      return;
    }

    String userId = jwtUtil.getUserId(token);
    String email = jwtUtil.getEmail(token);
    String accountType = jwtUtil.getAccountType(token);
    String role = jwtUtil.getRole(token);
    String expired = String.valueOf(jwtUtil.isExpired(token));

    System.out.println("userId = " + userId);
    System.out.println("role = " + role);
    System.out.println(expired);
    System.out.println("email = " + email);

    UserResponseDTO userResponseDTO = new UserResponseDTO();
    userResponseDTO.setUserId(userId);
    userResponseDTO.setEmail(email);
    userResponseDTO.setAccountType(accountType);
    userResponseDTO.setRole(UserRoleType.USER);

    Authentication authToken;

    if ("SOCIAL".equals(accountType)) {
      CustomOAuth2User customOAuth2User = new CustomOAuth2User(userResponseDTO);
      authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null,
          customOAuth2User.getAuthorities());
    } else {
      User user = new User();
      user.setUserId(userId);
      user.setEmail(email);
      user.setAccountType(accountType);
      user.setRole(UserRoleType.USER);
      CustomUserDetails customUserDetails = new CustomUserDetails(user);
      authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
          customUserDetails.getAuthorities());
    }

    SecurityContextHolder.getContext().setAuthentication(authToken);

    filterChain.doFilter(request, response);

  }
}

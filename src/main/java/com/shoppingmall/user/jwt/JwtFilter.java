package com.shoppingmall.user.jwt;

import com.shoppingmall.oauth2.model.CustomOAuth2User;
import com.shoppingmall.user.dto.UserResponseDTO;
import com.shoppingmall.user.model.UserRoleType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JwtFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Authorization")) {
                    authorization = cookie.getValue();
                }
            }
        }
        if(authorization == null) {
            System.out.println("token is null");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization;

        if(jwtUtil.isExpired(token)) {
            System.out.println("token is expired");
            filterChain.doFilter(request, response);
            return;
        }

        String userId = jwtUtil.getUserId(token);
        String role = jwtUtil.getRole(token);
        String expired = String.valueOf(jwtUtil.isExpired(token));

        System.out.println("userId = " + userId);
        System.out.println("role = " + role);
        System.out.println(expired);
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUserId(userId);
        userResponseDTO.setRole(UserRoleType.valueOf(role));

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userResponseDTO);

        Authentication authToken =  new UsernamePasswordAuthenticationToken(customOAuth2User , null , customOAuth2User.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }
}

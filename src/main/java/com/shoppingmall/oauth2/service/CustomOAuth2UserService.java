package com.shoppingmall.oauth2.service;

import com.shoppingmall.oauth2.dto.KakaoResponse;
import com.shoppingmall.oauth2.model.CustomOAuth2User;
import com.shoppingmall.oauth2.dto.GoogleResponse;
import com.shoppingmall.oauth2.dto.NaverResponse;
import com.shoppingmall.oauth2.dto.OAuth2Response;
import com.shoppingmall.user.dto.PasswordGenerator;
import com.shoppingmall.user.model.User;
import com.shoppingmall.user.model.UserRoleType;
import com.shoppingmall.user.repository.UserRepository;
import com.shoppingmall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private PasswordGenerator passwordGenerator;
    @Autowired
    private UserRepository userRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 네이버 혹은 구글인지 식별을 위한 데이터 로드
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if(registrationId.equals("naver")){
             oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }else if(registrationId.equals("google")){
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }else if(registrationId.equals("kakao")){
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }else{
            return null;
        }
        // 소셜로그인 디버깅용
        if (oAuth2Response != null) {
            System.out.println("Provider: " + oAuth2Response.getProvider());
            System.out.println("ProviderId: " + oAuth2Response.getProviderId());
            System.out.println("Email: " + oAuth2Response.getEmail());
            System.out.println("Name: " + oAuth2Response.getName());
        } else {
            System.out.println("Failed to create OAuth2Response for " + registrationId);
        }

        User existingUser = userRepository.findByEmail(oAuth2Response.getEmail());

        if(existingUser == null) {
            User user = new User();
            user.setUserId( oAuth2Response.getProvider()+"&"+oAuth2Response.getProviderId());
            user.setEmail(oAuth2Response.getEmail());
            user.setPassword(passwordGenerator.generateTemporaryPassword());
            user.setNickname(oAuth2Response.getProvider()+"_"+oAuth2Response.getProviderId().substring(0,4));
            user.setRole(UserRoleType.USER);
            userRepository.save(user);

            return new CustomOAuth2User(oAuth2Response , UserRoleType.USER);
        }else{
            return new CustomOAuth2User(oAuth2Response , existingUser.getRole());
        }

     }
}

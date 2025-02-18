package com.shoppingmall.user.service;

import com.shoppingmall.user.dto.CustomOAuth2User;
import com.shoppingmall.user.dto.GoogleResponse;
import com.shoppingmall.user.dto.NaverResponse;
import com.shoppingmall.user.dto.OAuth2Response;
import com.shoppingmall.user.model.UserRoleType;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());
        // 네이버 혹은 구글인지 식별을 위한 데이터 로드
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if(registrationId.equals("naver")){
             oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }else if(registrationId.equals("google")){
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }else{
            return null;
        }

        UserRoleType role = UserRoleType.USER;
        return new CustomOAuth2User(oAuth2Response , role);
     }
}

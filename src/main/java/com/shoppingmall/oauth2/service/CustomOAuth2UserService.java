package com.shoppingmall.oauth2.service;

import com.shoppingmall.oauth2.dto.KakaoResponse;
import com.shoppingmall.oauth2.model.CustomOAuth2User;
import com.shoppingmall.oauth2.dto.GoogleResponse;
import com.shoppingmall.oauth2.dto.NaverResponse;
import com.shoppingmall.oauth2.dto.OAuth2Response;
import com.shoppingmall.user.dto.PasswordGenerator;
import com.shoppingmall.user.dto.UserResponseDTO;
import com.shoppingmall.user.model.User;
import com.shoppingmall.user.model.UserRoleType;
import com.shoppingmall.user.repository.UserRepository;
import com.shoppingmall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final PasswordGenerator passwordGenerator;
    private final UserRepository userRepository;

    public CustomOAuth2UserService(PasswordGenerator passwordGenerator, UserRepository userRepository) {
        this.passwordGenerator = passwordGenerator;
        this.userRepository = userRepository;
    }


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

        String userId = oAuth2Response.getProvider()+ " " +oAuth2Response.getProviderId();
        User existingUser = userRepository.findByEmail(oAuth2Response.getEmail());

        if(existingUser == null) {
            User user = new User();
            user.setUserId(userId);
            user.setEmail(oAuth2Response.getEmail());
            user.setPassword(passwordGenerator.generateTemporaryPassword());
            user.setNickname(oAuth2Response.getProvider()+"_"+oAuth2Response.getProviderId().substring(0,4));
            user.setRole(UserRoleType.USER);

            userRepository.save(user);

            return new CustomOAuth2User(user.toDTO());
        }else{
            existingUser.setNickname(oAuth2Response.getName());
            userRepository.save(existingUser);

            return new CustomOAuth2User(existingUser.toDTO());
        }

     }
}

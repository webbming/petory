package com.shoppingmall.oauth2.service;

import com.shoppingmall.oauth2.dto.KakaoResponse;
import com.shoppingmall.oauth2.model.CustomOAuth2User;
import com.shoppingmall.oauth2.dto.GoogleResponse;
import com.shoppingmall.oauth2.dto.NaverResponse;
import com.shoppingmall.oauth2.dto.OAuth2Response;
import com.shoppingmall.user.utils.PasswordGenerator;
import com.shoppingmall.user.model.User;
import com.shoppingmall.user.model.UserRoleType;
import com.shoppingmall.user.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final PasswordGenerator passwordGenerator;
  private final UserRepository userRepository;

  public CustomOAuth2UserService(PasswordGenerator passwordGenerator,
      UserRepository userRepository) {
    this.passwordGenerator = passwordGenerator;
    this.userRepository = userRepository;
  }


  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    // 네이버 혹은 구글인지 식별을 위한 데이터 로드
    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    OAuth2Response oAuth2Response = null;

    if (registrationId.equals("naver")) {
      oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
    } else if (registrationId.equals("google")) {
      oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
    } else if (registrationId.equals("kakao")) {
      oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
    } else {
      return null;
    }

    // 소셜 로그인 유저의 이메일과 똑같은 유저가 db에 존재하는지 확인.
    User newUser = userRepository.findByEmailAndAccountType(oAuth2Response.getEmail(), "SOCIAL");
    // 존재하지 않는다면  생성하고 소셜로그인 유저의 정보를 저장
    if (newUser == null) {

      User user = new User();
      user.setUserId(oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId());
      user.setEmail(oAuth2Response.getEmail());
      user.setPassword(passwordGenerator.generateTemporaryPassword());
      user.setNickname(oAuth2Response.getProvider() + "_" + oAuth2Response.getName());
      user.setAccountType("SOCIAL");
      user.setRole(UserRoleType.USER);

      userRepository.save(user);

      return new CustomOAuth2User(user.toDTO());
      // 존재하면 저장하지 않고 닉네임만 업데이트 한 후에 return
    } else {
      System.out.println(newUser.getUserId());
      return new CustomOAuth2User(newUser.toDTO());
    }

  }
}

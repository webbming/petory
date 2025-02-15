package com.shoppingmall.user.service;

import com.shoppingmall.user.dto.UserRequestDTO;
import com.shoppingmall.user.model.User;
import com.shoppingmall.user.repository.UserRepository;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private UserRepository userRepository;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public UserService(UserRepository userRepository , BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  public boolean checkDuplicate(String field, String value) {
    boolean isDuplicate = switch (field) {
      case "userId" -> userRepository.existsByUserId(value);
      case "email" -> userRepository.existsByEmail(value);
      case "nickname" -> userRepository.existsByNickname(value);
      default -> throw new IllegalStateException("Unexpected value: " + field);
    };
    return isDuplicate;
  }

  //유저 생성
  public void registerUser(UserRequestDTO userDTO) {
    userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
    User user  = userDTO.toEntity();
    userRepository.save(user);
  }


  //유저 수정
  public void updateUser(UserRequestDTO userDTO) {

  }


  // 유저 조회
  public Optional<User> getUser(String userId) {
    return null;
  }


  // 유저 삭제
  public void deleteUser(String userId) {

  }



}

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

  public boolean checkDuplicate(String fieldName, String fieldValue) {
    boolean isDuplicate = switch (fieldName) {
      case "userId" -> userRepository.existsByUserId(fieldValue);
      case "email" -> userRepository.existsByEmail(fieldValue);
      case "nickname" -> userRepository.existsByNickname(fieldValue);
      default -> throw new IllegalStateException("Unexpected value: " + fieldName);
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

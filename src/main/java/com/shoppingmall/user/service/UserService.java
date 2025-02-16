package com.shoppingmall.user.service;

import com.shoppingmall.user.dto.UserRequestDTO;
import com.shoppingmall.user.dto.UserResponseDTO;
import com.shoppingmall.user.dto.UserUpdateDTO;
import com.shoppingmall.user.exception.DuplicateException;
import com.shoppingmall.user.model.User;
import com.shoppingmall.user.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

@Service
public class UserService {

  private UserRepository userRepository;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public UserService(UserRepository userRepository , BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  // 필드 유효성 검사
  public Map<String,String> filedErrorsHandler(Errors errors){
    Map<String,String> errorMap = new HashMap<>();
    for(FieldError error : errors.getFieldErrors()){
      errorMap.put(error.getField(), error.getDefaultMessage());
    }
    return errorMap;
  }

  // 개별 필드 검사
  public boolean checkDuplicate(String fieldName, String fieldValue) {
    Map<String,String> errors = new HashMap<>();
    boolean isDuplicate = switch (fieldName) {
      case "userId" -> userRepository.existsByUserId(fieldValue);
      case "email" -> userRepository.existsByEmail(fieldValue);
      case "nickname" -> userRepository.existsByNickname(fieldValue);
      default -> throw new IllegalStateException("Unexpected value: " + fieldName);
    };
    return isDuplicate;
  }

  // 회원가입 요청시 최종 중복 검사
  public void checkDuplicate(UserRequestDTO userDTO) {
    Map<String,String> errors = new HashMap<>();
    if(userRepository.existsByUserId(userDTO.getUserId())){
      errors.put("userId", "이미 사용 중인 아이디입니다.");
    }
    if(userRepository.existsByUserId(userDTO.getEmail())){
      errors.put("email", "이미 사용 중인 이메일입니다.");
    }
    if(userRepository.existsByUserId(userDTO.getNickname())){
      errors.put("nickname", "이미 사용 중인 닉네임입니다.");
    }
    if(!errors.isEmpty()){
      throw new DuplicateException(errors);
    }
  }

  //유저 생성
  public void registerUser(UserRequestDTO userDTO) {
    checkDuplicate(userDTO);
    userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
    User user  = userDTO.toEntity();
    userRepository.save(user);
  }

  // 유저 조회
  public UserResponseDTO getUser(String userId) {

    User user = userRepository.findByUserId(userId);
    if(user == null) {
      throw new UsernameNotFoundException("User not found");
    }

    return user.toDTO();
  }

  //유저 수정
  public void updateUser(UserUpdateDTO userDTO) {


    User user = userRepository.findByUserId(userDTO.getUserId());

    user.setNickname(userDTO.getNickname());
    user.setEmail(userDTO.getEmail());
    user.setAddress(userDTO.getAddress());
    userRepository.save(user);
  }





  // 유저 삭제
  public void deleteUser(String userId) {

  }



}

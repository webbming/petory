package com.shoppingmall.user.service;

import com.shoppingmall.user.dto.UserRequestDTO;
import com.shoppingmall.user.dto.UserResponseDTO;
import com.shoppingmall.user.dto.UserUpdateDTO;
import com.shoppingmall.user.model.User;
import com.shoppingmall.user.repository.UserRepository;

import java.util.HashMap;
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

  public Map<String,String> filedErrorsHandler(Errors errors){

    Map<String,String> errorMap = new HashMap<>();
    for(FieldError error : errors.getFieldErrors()){
      errorMap.put(error.getField(), error.getDefaultMessage());
    }
    return errorMap;
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
    if(userRepository.existsByUserId(userDTO.getUserId())){
        throw new IllegalStateException("User already exists");
    }
    if(userRepository.existsByEmail(userDTO.getEmail())){
      throw new IllegalStateException("Email already exists");
    }
    if(userRepository.existsByUserId(userDTO.getNickname())){
      throw new IllegalStateException("Nickname already exists");
    }


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
    if(userRepository.existsByEmail(userDTO.getEmail())){
      throw new IllegalStateException("Email already exists");
    }
    if(userRepository.existsByUserId(userDTO.getNickname())){
      throw new IllegalStateException("Nickname already exists");
    }
    user.setNickname(userDTO.getNickname());
    user.setEmail(userDTO.getEmail());
    user.setAddress(userDTO.getAddress());
    userRepository.save(user);
  }





  // 유저 삭제
  public void deleteUser(String userId) {

  }



}

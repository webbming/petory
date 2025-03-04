package com.shoppingmall.user.service;

import static org.junit.jupiter.api.Assertions.*;

import com.shoppingmall.user.model.User;
import com.shoppingmall.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@Rollback(false)
class UserServiceTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  void registerUser() {

    User user = new User();
    user.setUserId("rlawlsgn22");
    user.setPassword("rlawlsgn22");
    user.setAccountType("NORMAL");
    user.setAddress("서울시 강동구 천호동");
    user.setEmail("rlawlsgn22@gmail.com");
    user.setAnswer("길동");
    user.setQuestion("q1");
    user.setNickname("길동아");
    /*given*/

    userRepository.save(user);
    userRepository.findByUserId(user.getUserId());
    /*when*/

    assertEquals("rlawlsgn22", user.getUserId());
    /*then*/

  }
}
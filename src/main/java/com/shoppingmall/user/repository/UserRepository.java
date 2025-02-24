package com.shoppingmall.user.repository;

import com.shoppingmall.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByEmailAndAccountType(String email, String accountType);
  User findByEmail(String email);
  User findByUserId(String userId);
  User findByUserIdAndEmail(String userId, String email);
  User findByQuestionAndAnswer(String question, String answer);
  Boolean existsByUserId(String userId);
  Boolean existsByEmail(String email);
  Boolean existsByNickname(String nickname);
  Boolean existsByEmailAndAccountType(String email , String accountType);

}

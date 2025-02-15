package com.shoppingmall.user.repository;

import com.shoppingmall.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  User findByUserId(String userId);
  Boolean existsByUserId(String userId);
  Boolean existsByEmail(String email);
  Boolean existsByNickname(String nickname);
}

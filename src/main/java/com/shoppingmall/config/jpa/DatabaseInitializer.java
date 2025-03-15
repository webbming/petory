package com.shoppingmall.config.jpa;

import com.shoppingmall.board.model.Board;
import com.shoppingmall.board.repository.BoardRepository;
import com.shoppingmall.product.model.Product;
import com.shoppingmall.product.repository.CategoryRepository;
import com.shoppingmall.product.repository.ProductRepository;
import com.shoppingmall.product.repository.SubcategoryRepository;
import com.shoppingmall.user.model.User;
import com.shoppingmall.user.model.UserRoleType;
import com.shoppingmall.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DatabaseInitializer(ProductRepository productRepository, CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository, BoardRepository boardRepository , UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // User가 존재하는지 확인하고 없으면 하나 생성
        if (userRepository.count() == 0) {
            User user = new User();
            user.setUserId("admin");  // 유저의 ID 지정
            user.setPassword(passwordEncoder.encode("1111"));  // 유저 비밀번호 설정
            user.setEmail("user1@example.com");  // 이메일 설정
            user.setNickname("어드민");  // 유저의 nickname 설정
            user.setAccountType("normal");  // 계정 유형 설정
            user.setRole(UserRoleType.ADMIN);  // 기본 사용자 역할 설정

            // User 생성 시 Cart와 UserImg 자동 생성되므로 추가 설정 필요 없음
            userRepository.save(user);
        }

        User user = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("User not found"));
        // Board 데이터가 이미 있으면 추가하지 않음
        if (boardRepository.count() == 0) {


            List<Board> boards = List.of(
                    new Board("게시글 1", "/images/1.jpg", "nickname1", "내용 1", "#hashtag1", "category1", user),
                    new Board("게시글 2", "/images/2.jpg", "nickname2", "내용 2", "#hashtag2", "category2", user),
                    new Board("게시글 3", "/images/3.jpg", "nickname3", "내용 3", "#hashtag3", "category3", user),
                    new Board("게시글 4", "/images/4.jpg", "nickname4", "내용 4", "#hashtag4", "category4", user),
                    new Board("게시글 5", "/images/5.jpg", "nickname5", "내용 5", "#hashtag5", "category5", user),
                    new Board("게시글 6", "/images/6.jpg", "nickname6", "내용 6", "#hashtag6", "category6", user),
                    new Board("게시글 7", "/images/7.jpg", "nickname7", "내용 7", "#hashtag7", "category7", user),
                    new Board("게시글 8", "/images/8.jpg", "nickname8", "내용 8", "#hashtag8", "category8", user),
                    new Board("게시글 9", "/images/9.jpg", "nickname9", "내용 9", "#hashtag9", "category9", user),
                    new Board("게시글 10", "/images/10.jpg", "nickname10", "내용 10", "#hashtag10", "category10", user)
            );


            boardRepository.saveAll(boards); // DB에 Board 데이터 삽입
        }
    }



}

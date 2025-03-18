package com.shoppingmall.config.jpa;

import com.shoppingmall.board.model.Board;
import com.shoppingmall.board.repository.BoardRepository;
import com.shoppingmall.product.model.Category;
import com.shoppingmall.product.model.PetType;
import com.shoppingmall.product.model.Product;
import com.shoppingmall.product.model.Subcategory;
import com.shoppingmall.product.repository.CategoryRepository;
import com.shoppingmall.product.repository.ProductRepository;
import com.shoppingmall.product.repository.SubcategoryRepository;
import com.shoppingmall.user.model.User;
import com.shoppingmall.user.model.UserRoleType;
import com.shoppingmall.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


// 초기 데이터 로드 클래스
// 유저 ADMIN 과 게시글 10개
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
                    new Board("강아지가 기침을 계속하네요...", null, user.getNickname(), "내용 1", null, "반슐랭", user),
                    new Board("다들 사료 어떤 브랜드 먹이세요?", null, user.getNickname(), "내용 2", null, "육아팁", user),
                    new Board("강아지가 좋아하는 장난감!", null, user.getNickname(), "내용 3", null, "상품후기", user),
                    new Board("강아지랑 갈만한 곳 추천좀요!", null, user.getNickname(), "내용 4", null, "육아팁", user),
                    new Board("슈나우저 피부병 ㅜㅜ", null, user.getNickname(), "내용 5", null, "반슐랭", user),
                    new Board("추천안누르면 너 신고", null, user.getNickname(), "내용 6", null, "일상공유", user),
                    new Board("추천좀 제발", null, user.getNickname(), "내용 7", null, "반슐랭", user),
                    new Board("귀여운 우리 강아지를 소개합니다", null, user.getNickname(), "내용 8", null, "육아팁", user),
                    new Board("강아지발바닥이 까졌어요 ㅜㅜ", null, user.getNickname(), "내용 9", null, "상품후기", user),
                    new Board("추천 100개 달성 시 패드 무료 나눔", null, user.getNickname(), "내용 10", null, "일상공유", user)
            );


            boardRepository.saveAll(boards); // DB에 Board 데이터 삽입
        }


        if (categoryRepository.count() == 0) {
            List<Category> categories = List.of(
                    new Category(null, "사료", PetType.DOG, new ArrayList<>())
            );
            categoryRepository.saveAll(categories); // 카테고리 저장
        }

// 저장된 카테고리를 가져옴
        Category category = categoryRepository.findByCategoryName("사료")
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (subcategoryRepository.count() == 0) {
            List<Subcategory> subcategories = List.of(
                    new Subcategory(null, category, "건식 사료", "강아지 건식 사료"),
                    new Subcategory(null, category, "습식 사료", "강아지 습식 사료")
            );
            subcategoryRepository.saveAll(subcategories);
        }

        if (productRepository.count() == 0) {
            Subcategory subcategory = subcategoryRepository.findBySubcategoryName("건식 사료")
                    .orElseThrow(() -> new RuntimeException("Subcategory not found"));

            List<Product> products = new ArrayList<>(); // ArrayList로 초기화
            products.add(new Product(
                    null, "고양이 화장실 청소용 스쿱", 25000, "스쿱", "스쿱", "고양이 모래에서 배변을 걸러내기 위한 스쿱",
                    0, new ArrayList<>(List.of("/images/product/1.jpg")), new ArrayList<>(), LocalDateTime.now(), BigDecimal.ZERO,
                    category, subcategory, PetType.DOG
            ));
            products.add(new Product(
                    null, "고양이 배변에 잘 붙는 모래!", 28000, "고양이 모래", "고양이 화장실에 이 모래를 써보세요", "이 모래는 아주 부드럽고 고양이들이 좋아해요",
                    0, new ArrayList<>(List.of("/images/product/2.jpg")), new ArrayList<>(), LocalDateTime.now(), BigDecimal.ZERO,
                    category, subcategory, PetType.DOG
            ));
            // 나머지 제품들도 동일한 방식으로 추가
            products.add(new Product(
                    null, "고양이들이 좋아하는 사료 유아용", 30000, "1", "고단백 사료", "근육 발달을 돕는 사료",
                    0, new ArrayList<>(List.of("/images/product/3.jpg")), new ArrayList<>(), LocalDateTime.now(), BigDecimal.ZERO,
                    category, subcategory, PetType.DOG
            ));
            products.add(new Product(
                    null, "고양이들이 좋아하는 사료 전연령", 22000, "전연령용", "오가닉 사료", "자연 원료로 만든 건강한 사료",
                    0, new ArrayList<>(List.of("/images/product/4.jpg")), new ArrayList<>(), LocalDateTime.now(), BigDecimal.ZERO,
                    category, subcategory, PetType.DOG
            ));
            products.add(new Product(
                    null, "반려견 반려묘 바닥 패드", 26000, "성묘/성견용", "패드", "패드",
                    0, new ArrayList<>(List.of("/images/product/11.jpg")), new ArrayList<>(), LocalDateTime.now(), BigDecimal.ZERO,
                    category, subcategory, PetType.CAT
            ));
            products.add(new Product(
                    null, "강아지 털방석 노랑색", 29000, "성견용", "강아지들이 좋아해요", "푹신푹신 방석",
                    0, new ArrayList<>(List.of("/images/product/12.jpg")), new ArrayList<>(), LocalDateTime.now(), BigDecimal.ZERO,
                    category, subcategory, PetType.CAT
            ));
            products.add(new Product(
                    null, "강아지 빗", 31000, "퍼피용", "강아지용 빗", "애견 빗",
                    0, new ArrayList<>(List.of("/images/product/13.jpg")), new ArrayList<>(), LocalDateTime.now(), BigDecimal.ZERO,
                    category, subcategory, PetType.CAT
            ));
            products.add(new Product(
                    null, "반려묘 반려견 담요", 32000, "1", "담요", "담요",
                    0, new ArrayList<>(List.of("/images/product/14.jpg")), new ArrayList<>(), LocalDateTime.now(), BigDecimal.ZERO,
                    category, subcategory, PetType.CAT
            ));

            productRepository.saveAll(products);  // DB에 Product 데이터 삽입
        }

    }

}

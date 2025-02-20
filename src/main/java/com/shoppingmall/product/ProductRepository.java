package com.shoppingmall.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoppingmall.category.Category;
import com.shoppingmall.category.Subcategory;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category); // 기본 카테고리 검색
    List<Product> findBySubcategory(Subcategory subcategory);
    List<Product> findByProductNameContaining(String productName);

    // 정렬별 메서드 추가
    List<Product> findByCategoryOrderByCreatedAtDesc(Category category);  // 최신순 (신제품순)
    List<Product> findByCategoryOrderByPriceAsc(Category category);       // 낮은 가격순
    List<Product> findByCategoryOrderByPriceDesc(Category category);      // 높은 가격순
    List<Product> findByCategoryOrderByAverageRatingDesc(Category category); // 평점순
}



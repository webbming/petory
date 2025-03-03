package com.shoppingmall.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shoppingmall.category.Category;
import com.shoppingmall.category.CategoryService;
import com.shoppingmall.category.Subcategory;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional  
public class ProductService {
	
    private final ProductRepository productRepository;  // 상품 데이터 접근을 위한 레포지토리
    private final CategoryService categoryService;  // 카테고리 관련 서비스

    // 전체 상품 목록 조회
    public List<Product> listAllProducts() {
        return productRepository.findAll();  // 모든 상품 조회
    }

    // 상품 등록 처리
    public Product saveProduct(Product product, Long categoryId, Long subcategoryId, List<String> imageUrls) {
        product.setCategory(categoryService.findCategoryById(categoryId));
        product.setSubcategory(categoryService.findSubcategoryById(subcategoryId));
        product.setImageUrls(imageUrls);  // 이미지 URL 리스트 설정
        return productRepository.save(product);
    }
    
    // 상품 ID로 상품 조회
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));  // 상품이 없으면 예외 발생
    }

    // 상품 수정 처리
    public Product updateProduct(Long id, Product newProduct, Long categoryId, Long subcategoryId, List<String> imageUrls) {
        Product product = getProductById(id);  // 수정할 상품 조회
        // 수정할 필드가 null이 아니면 값을 변경
        Optional.ofNullable(newProduct.getProductName()).ifPresent(product::setProductName);
        Optional.ofNullable(newProduct.getPrice()).ifPresent(product::setPrice);
        Optional.ofNullable(newProduct.getOption()).ifPresent(product::setOption);
        Optional.ofNullable(newProduct.getContent()).ifPresent(product::setContent);
        Optional.ofNullable(newProduct.getDescription()).ifPresent(product::setDescription);
        Optional.ofNullable(newProduct.getImageUrl()).ifPresent(product::setImageUrl);
        product.setImageUrls(imageUrls);  // 이미지 URL 리스트 업데이트
        product.setCategory(categoryService.findCategoryById(categoryId));  // 카테고리 업데이트
        product.setSubcategory(categoryService.findSubcategoryById(subcategoryId));  // 서브카테고리 업데이트
        
        return productRepository.save(product);  // 수정된 상품 저장
    }
    
    // 상품 삭제 처리
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);  // 상품 삭제
    }
    
    // 카테고리 이름으로 상품 조회
    public List<Product> findProductsByCategoryName(String categoryName) {
    	Category category = categoryService.findCategoryByName(categoryName);  // 카테고리 조회
    	return productRepository.findByCategory(category);  // 해당 카테고리에 속하는 상품 조회
    }

    // 상품명으로 상품 검색
    public List<Product> searchProducts(String keyword) {
    	return productRepository.findByProductNameContaining(keyword);  // 상품명이 키워드를 포함하는 상품 검색
    }
    
    // 카테고리별로 상품 정렬 조회
    public List<Product> findProductsByCategory(Category category, String sort) {
        switch (sort) {
            case "priceLowHigh":  // 가격 오름차순
                return productRepository.findByCategoryOrderByPriceAsc(category);
            case "priceHighLow":  // 가격 내림차순
                return productRepository.findByCategoryOrderByPriceDesc(category);
            case "rating":  // 평점 내림차순
                return productRepository.findByCategoryOrderByAverageRatingDesc(category);
            default:  // 기본 정렬: 신제품순
                return productRepository.findByCategoryOrderByCreatedAtDesc(category);
        }
    }
 
    // 전체 상품을 정렬 기준에 맞춰 조회
    public List<Product> listAllProductsSorted(String sort) {
        switch (sort) {
            case "priceLowHigh":  // 가격 오름차순
                return productRepository.findAll(Sort.by(Sort.Direction.ASC, "price"));
            case "priceHighLow":  // 가격 내림차순
                return productRepository.findAll(Sort.by(Sort.Direction.DESC, "price"));
            case "rating":  // 평점 내림차순
                return productRepository.findAll(Sort.by(Sort.Direction.DESC, "averageRating"));
            default:  // 기본 정렬: 신제품순
                return productRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        }
    }

    // 서브카테고리별로 상품 정렬 조회
    public List<Product> findProductsBySubcategory(Subcategory subcategory, String sort) {
        switch (sort) {
            case "priceLowHigh":  // 가격 오름차순
                return productRepository.findBySubcategoryOrderByPriceAsc(subcategory);
            case "priceHighLow":  // 가격 내림차순
                return productRepository.findBySubcategoryOrderByPriceDesc(subcategory);
            case "rating":  // 평점 내림차순
                return productRepository.findBySubcategoryOrderByAverageRatingDesc(subcategory);
            default:  // 기본 정렬: 신제품순
                return productRepository.findBySubcategoryOrderByCreatedAtDesc(subcategory);
        }
    }
}

package com.shoppingmall.category;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shoppingmall.product.Product;
import com.shoppingmall.product.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
	
	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;
	private final SubcategoryRepository subcategoryRepository;
	
	public List<Category> findAllCategories() {
		return categoryRepository.findAll();
	}
	
	public List<Subcategory> findAllSubcategories() {
		return subcategoryRepository.findAll();
	}
	
	public Category findCategoryByName(String categoryName) {
		return categoryRepository.findByCategoryName(categoryName)
				.orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));
	}
	
	public Category findCategoryById(Long id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));
	}

	// 서브카테고리 이름
	public Subcategory findSubcategoryByName(String subcategoryName) {
		return subcategoryRepository.findBySubcategoryName(subcategoryName)
				.orElseThrow(() -> new RuntimeException("서브카테고리를 찾을 수 없습니다."));
	}
	
	public List<Subcategory> findSubcategoriesByCategory(Category category) {
	    return subcategoryRepository.findByCategory(category);
	}
	
	public Subcategory findSubcategoryById(Long id) {
		return subcategoryRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("서브카테고리를 찾을 수 없습니다."));
	}

	// 카테고리상품 검색
	public List<Product> findProductsByCategory(Category category) {
	    return productRepository.findByCategoryOrderByCreatedAtDesc(category);
	}


	// 서브카테고리기반상품 검색
	public List<Product> findProductsBySubcategory(Subcategory subcategory) {
		return productRepository.findBySubcategory(subcategory);
	}
	
	public List<Product> findProductsByCategorySorted(Category category, String sort) {
	    switch (sort) {
	        case "priceLowHigh":
	            return productRepository.findByCategoryOrderByPriceAsc(category);
	        case "priceHighLow":
	            return productRepository.findByCategoryOrderByPriceDesc(category);
	        case "rating":
	            return productRepository.findByCategoryOrderByAverageRatingDesc(category);
	        default:
	            return productRepository.findByCategoryOrderByCreatedAtDesc(category); // 기본 정렬: 신제품순
	    }
	}


}

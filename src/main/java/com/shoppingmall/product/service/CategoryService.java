package com.shoppingmall.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.shoppingmall.product.model.Category;
import com.shoppingmall.product.model.PetType;
import com.shoppingmall.product.model.Product;
import com.shoppingmall.product.model.Subcategory;
import com.shoppingmall.product.repository.CategoryRepository;
import com.shoppingmall.product.repository.ProductRepository;
import com.shoppingmall.product.repository.SubcategoryRepository;

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

	// petType에 따른 카테고리 조회
    public List<Category> findCategoriesByPetType(PetType petType) {
        return categoryRepository.findByPetType(petType);
    }
    
    // petType에 따른 서브카테고리 조회 (Category의 petType 필드를 이용)
    public List<Subcategory> findSubcategoriesByPetType(PetType petType) {
        return subcategoryRepository.findByCategory_PetType(petType);
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
	// 상품 카테고리 생성
	public Category createCategory(String categoryName, PetType petType) {
	    Category newCategory = new Category();
	    newCategory.setCategoryName(categoryName);
	    newCategory.setPetType(petType); // ✅ PetType도 저장하도록 수정
	    return categoryRepository.save(newCategory);
	}

	
	// 상품 서브카테고리 생성
	public Subcategory createSubcategory(Long categoryId, String subcategoryName) {
	    Category category = findCategoryById(categoryId);
	    
	    // 중복 체크
	    Optional<Subcategory> existingSubcategory = subcategoryRepository.findBySubcategoryNameAndCategory(subcategoryName, category);
	    if (existingSubcategory.isPresent()) {
	        throw new RuntimeException("이미 존재하는 서브카테고리입니다.");
	    }

	    Subcategory newSubcategory = new Subcategory();
	    newSubcategory.setCategory(category);
	    newSubcategory.setSubcategoryName(subcategoryName);
	    return subcategoryRepository.save(newSubcategory);
	}

	
	// 카테고리 삭제
	public void deleteCategory(Long categoryId) {
	    Category category = findCategoryById(categoryId);
	    
	    // 서브카테고리 삭제
	    List<Subcategory> subcategories = findSubcategoriesByCategory(category);
	    subcategoryRepository.deleteAll(subcategories);

	    // 해당 카테고리에 속한 상품 삭제
	    List<Product> products = productRepository.findByCategory(category);
	    productRepository.deleteAll(products);

	    // 카테고리 삭제
	    categoryRepository.delete(category);
	}


	
	// 서브 카테고리 삭제
	public void deleteSubcategory(Long subcategoryId) {
		subcategoryRepository.deleteById(subcategoryId);
	}
}

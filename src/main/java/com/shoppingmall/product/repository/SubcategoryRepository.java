package com.shoppingmall.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoppingmall.product.model.Category;
import com.shoppingmall.product.model.PetType;
import com.shoppingmall.product.model.Subcategory;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
	Optional<Subcategory> findBySubcategoryName(String subcategoryName);
	List<Subcategory> findByCategory(Category category);
	Optional<Subcategory> findBySubcategoryNameAndCategory(String subcategoryName, Category category);
	List<Subcategory> findByCategory_PetType(PetType petType);
}

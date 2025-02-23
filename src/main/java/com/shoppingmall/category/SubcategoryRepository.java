package com.shoppingmall.category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
	Optional<Subcategory> findBySubcategoryName(String subcategoryName);
	List<Subcategory> findByCategory(Category category);
}

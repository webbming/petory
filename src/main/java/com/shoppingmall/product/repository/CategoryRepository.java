package com.shoppingmall.product.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoppingmall.product.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	Optional<Category> findByCategoryName(String categoryName);
}

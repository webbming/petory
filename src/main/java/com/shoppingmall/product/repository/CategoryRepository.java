package com.shoppingmall.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoppingmall.product.model.Category;
import com.shoppingmall.product.model.PetType;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	Optional<Category> findByCategoryName(String categoryName);
	List<Category> findByPetType(PetType petType);
}

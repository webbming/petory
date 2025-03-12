package com.shoppingmall.product.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long categoryId;
	
	@Column(name = "category_name", nullable = false, unique = true) 
	private String categoryName;
	
	// 반려동물 타입(고양이/강아지)
	@Column(name = "pet_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private PetType petType;
	
	// 서브카테고리 목록 관리용으로 추가
	@OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
	private List<Subcategory> subcategories;
}

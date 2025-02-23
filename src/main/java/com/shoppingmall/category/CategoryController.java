package com.shoppingmall.category;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shoppingmall.product.Product;
import com.shoppingmall.product.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;
	private final ProductService productService;
	
	@GetMapping
	public String listCategories(Model model) {
		List<Category> categories = categoryService.findAllCategories();
		model.addAttribute("categories", categories);
		return "category/categoryList";
	}
	
	@GetMapping("/{categoryName}")
	public String viewCategoryProducts(@PathVariable String categoryName, Model model) {
		List<Product> products = productService.findProductsByCategoryName(categoryName);
		model.addAttribute("categoryName", categoryName);
		model.addAttribute("products", products);
		model.addAttribute("categories", categoryService.findAllCategories());
		return "category/categoryProducts";
	}
	
	@GetMapping("/subcategories/{categoryId}")
	public ResponseEntity<List<Subcategory>> getSubcategoriesByCategory(@PathVariable Long categoryId) {
	    Category category = categoryService.findCategoryById(categoryId);
	    List<Subcategory> subcategories = categoryService.findSubcategoriesByCategory(category);
	    return ResponseEntity.ok(subcategories);
	}

}

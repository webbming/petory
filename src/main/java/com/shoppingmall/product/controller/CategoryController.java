package com.shoppingmall.product.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shoppingmall.product.model.Category;
import com.shoppingmall.product.model.PetType;
import com.shoppingmall.product.model.Product;
import com.shoppingmall.product.model.Subcategory;
import com.shoppingmall.product.service.CategoryService;
import com.shoppingmall.product.service.ProductService;

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
	
	@GetMapping("/subcategories/{categoryId}/json")
	public ResponseEntity<List<Subcategory>> getSubcategoriesByCategory(@PathVariable Long categoryId) {
	    Category category = categoryService.findCategoryById(categoryId);
	    List<Subcategory> subcategories = categoryService.findSubcategoriesByCategory(category);
	    return ResponseEntity.ok(subcategories);
	}
	// 카테고리 추가
	@PostMapping("/add")
    public String addCategory(@RequestParam String categoryName, @RequestParam PetType petType,  RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryService.createCategory(categoryName, petType);
            redirectAttributes.addFlashAttribute("successMessage", "카테고리 '" + categoryName + "'가 성공적으로 추가되었습니다.");
            return "redirect:/categories";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "카테고리 추가 실패: " + e.getMessage());
            return "redirect:/categories";
        }
    }
	// 서브 카테고리 추가
	@PostMapping("/subcategories/add")
	public String addSubcategory(@RequestParam Long categoryId, 
	                             @RequestParam String subcategoryName, 
	                             RedirectAttributes redirectAttributes) {
	    try {
	        categoryService.createSubcategory(categoryId, subcategoryName);
	        redirectAttributes.addFlashAttribute("successMessage", "서브카테고리 추가 성공!");
	        return "redirect:/categories";
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "서브카테고리 추가 실패: " + e.getMessage());
	        return "redirect:/categories";
	    }
	}
    
 // 카테고리 삭제
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.ok().body("카테고리가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("카테고리 삭제 실패: " + e.getMessage());
        }
    }

    // 서브 카테고리 삭제
    @DeleteMapping("/{categoryId}/subcategories/delete/{subcategoryId}")
    public ResponseEntity<?> deleteSubcategory(@PathVariable Long categoryId, @PathVariable Long subcategoryId) {
        try {
            categoryService.deleteSubcategory(subcategoryId);
            return ResponseEntity.ok().body("서브카테고리가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("서브카테고리 삭제 실패: " + e.getMessage());
        }
    }
}

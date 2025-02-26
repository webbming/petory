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
	
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    
    public List<Product> listAllProducts() {
        return productRepository.findAll();
    }

    public Product saveProduct(Product product, Long categoryId, Long subcategoryId) {
        product.setCategory(categoryService.findCategoryById(categoryId));
        product.setSubcategory(categoryService.findSubcategoryById(subcategoryId));
        return productRepository.save(product);
    }
    
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
    }

    public Product updateProduct(Long id, Product newProduct, Long categoryId, Long subcategoryId) {
        Product product = getProductById(id);
        Optional.ofNullable(newProduct.getProductName()).ifPresent(product::setProductName);
        Optional.ofNullable(newProduct.getPrice()).ifPresent(product::setPrice);
        Optional.ofNullable(newProduct.getOption()).ifPresent(product::setOption);
        Optional.ofNullable(newProduct.getContent()).ifPresent(product::setContent);
        Optional.ofNullable(newProduct.getDescription()).ifPresent(product::setDescription);
        Optional.ofNullable(newProduct.getImageUrl()).ifPresent(product::setImageUrl);
        
        product.setCategory(categoryService.findCategoryById(categoryId));
        product.setSubcategory(categoryService.findSubcategoryById(subcategoryId));
        
        return productRepository.save(product);
    }
    
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    public List<Product> findProductsByCategoryName(String categoryName) {
    	Category category = categoryService.findCategoryByName(categoryName);
    	return productRepository.findByCategory(category);
    }

    public List<Product> searchProducts(String keyword) {
    	return productRepository.findByProductNameContaining(keyword);
    }
    
    public List<Product> findProductsByCategory(Category category, String sort) {
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
 
    public List<Product> listAllProductsSorted(String sort) {
        switch (sort) {
            case "priceLowHigh":
                return productRepository.findAll(Sort.by(Sort.Direction.ASC, "price"));
            case "priceHighLow":
                return productRepository.findAll(Sort.by(Sort.Direction.DESC, "price"));
            case "rating":
                return productRepository.findAll(Sort.by(Sort.Direction.DESC, "averageRating"));
            default:
                return productRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")); // 신제품순
        }
    }

    public List<Product> findProductsBySubcategory(Subcategory subcategory, String sort) {
        switch (sort) {
            case "priceLowHigh":
                return productRepository.findBySubcategoryOrderByPriceAsc(subcategory);
            case "priceHighLow":
                return productRepository.findBySubcategoryOrderByPriceDesc(subcategory);
            case "rating":
                return productRepository.findBySubcategoryOrderByAverageRatingDesc(subcategory);
            default:
                return productRepository.findBySubcategoryOrderByCreatedAtDesc(subcategory); // 기본 정렬: 신제품순
        }
    }

}
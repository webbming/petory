package com.shoppingmall.product;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.shoppingmall.category.Category;
import com.shoppingmall.category.CategoryService;
import com.shoppingmall.category.Subcategory;
import com.shoppingmall.review.Review;
import com.shoppingmall.review.ReviewService;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;

    public ProductController(ProductService productService, CategoryService categoryService, ReviewService reviewService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.reviewService = reviewService;
    }

    // 메인 페이지 (전체 상품)
    @GetMapping({"/product", "/products"})
    public String listProducts(@RequestParam(defaultValue = "newest") String sort, Model model) {
        // 전체상품,모든카테고리, 필터링
    	List<Product> products = productService.listAllProductsSorted(sort);
        model.addAttribute("products", productService.listAllProducts());
        model.addAttribute("categories", categoryService.findAllCategories());
        return "product/index2";
    }

    // 상품 등록폼
    @GetMapping("/products/add")
    public String addProductForm(Model model) {
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("subcategories", categoryService.findAllSubcategories());
        model.addAttribute("product", new Product());
        return "product/addProduct";
    }

    // 상품 등록처리
    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute("product") Product product,
                             @RequestParam("categoryId") Long categoryId,
                             @RequestParam("subcategoryId") Long subcategoryId,
                             @RequestParam("imageFile") MultipartFile file) {
        String imageUrl = uploadFile(file);
        product.setImageUrl(imageUrl);
        productService.saveProduct(product, categoryId, subcategoryId);
        return "redirect:/products";
    }
    
    private String uploadFile(MultipartFile file) {
        if (!file.isEmpty()) {
            String basePath = new File("src/main/resources/static/images").getAbsolutePath();
            String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            String filePath = basePath + File.separator + fileName;
            File destinationFile = new File(filePath);
            try {
                file.transferTo(destinationFile);
                return "/images/" + fileName;
            } catch (IOException e) {
                System.out.println("Error during file upload: " + e.getMessage());
                return null;
            }
        }
        return null;
    }

    @GetMapping("/products/{id}")
    public String viewProduct(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        List<Review> reviews = reviewService.getReviewsByProductId(id);
        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        return "product/productDetail";
    }


    // 상품 수정폼
    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        var product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("subcategories", categoryService.findAllSubcategories());
        return "product/editProduct";
    }

 // 상품 수정처리
    @PostMapping("/products/edit/{id}")
    public String editProduct(@PathVariable(name = "id") Long id,
                              @ModelAttribute("product") Product product,
                              @RequestParam(name = "categoryId") Long categoryId,
                              @RequestParam(name = "subcategoryId") Long subcategoryId,
                              @RequestParam(name = "imageFile") MultipartFile file) {
        String imageUrl = uploadFile(file);
        product.setImageUrl(imageUrl);  // 새로운 이미지 URL을 상품 정보에 설정
        productService.updateProduct(id, product, categoryId, subcategoryId);
        return "redirect:/products";
    }

    // 상품 삭제처리
    @DeleteMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    @GetMapping("/products/category/{categoryId}")
    public String getProductsByCategory(@PathVariable("categoryId") Long categoryId,
                                        @RequestParam(required = false, defaultValue = "newest") String sort,
                                        Model model) {
        Category category = categoryService.findCategoryById(categoryId);
        List<Product> products = productService.findProductsByCategory(category, sort);

        List<Subcategory> subcategories = categoryService.findSubcategoriesByCategory(category);

        model.addAttribute("currentCategory", category);
        model.addAttribute("products", products);
        model.addAttribute("subcategories", subcategories);
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("sort", sort); // 정렬 기준 유지
        System.out.println("Sort parameter received: " + sort);
        return "product/index2";
    }

    @GetMapping("/products/category/{categoryId}/json")
    public ResponseEntity<List<Product>> getProductsByCategoryJson(
            @PathVariable("categoryId") Long categoryId,
            @RequestParam(defaultValue = "newest") String sort) {
        Category category = categoryService.findCategoryById(categoryId);
        List<Product> products = productService.findProductsByCategory(category, sort);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/subcategory/{subId}")
    public String getProductsBySubcategory(@PathVariable("subId") Long subId, Model model) {
        Subcategory sc = categoryService.findSubcategoryById(subId);
        List<Product> products = categoryService.findProductsBySubcategory(sc);
        Category parentCategory = sc.getCategory();
        List<Subcategory> subcats = categoryService.findSubcategoriesByCategory(parentCategory);

        model.addAttribute("currentCategory", parentCategory);
        model.addAttribute("products", products);
        model.addAttribute("subcategories", subcats);
        model.addAttribute("categories", categoryService.findAllCategories());

        return "product/index2";
    }

    //상품 검색(상품이름)
    @GetMapping("/products/search")
    public String searchProducts(@RequestParam("search") String search, Model model) {
    	List<Product> products = productService.searchProducts(search);
    	model.addAttribute("products", products);
    	return "product/index2";
    }
    

    //상품 리뷰등록
    @PostMapping("/products/{productId}/reviews")
    public String addReview(@PathVariable Long productId, @ModelAttribute Review review) {
    	review.setProductId(productId);
    	//테스트 목적 고정 userId
    	review.setUserId(1L);
    	reviewService.saveReview(review);
    	//로그인 한 사용자 ID 반환
    	//review.setUserId(getUserIdFromPrincipal(principal));
    	return "redirect:/products/" + productId;
    }
    
    // 상품 리뷰 조회 API
    @GetMapping("/prodycts/{productId}/reviews")
    public ResponseEntity<List<Review>> getReviews(@PathVariable Long productId) {
    	List<Review> reviews = reviewService.getReviewsByProductId(productId);
    	return ResponseEntity.ok(reviews);
    }
    
    // 리뷰 로그인 사용자ID 반환 
    //private Long getUserIdFromPrincipal(Princpal princpal) {}
}

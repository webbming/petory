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

import io.swagger.v3.oas.annotations.Operation;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;

    // 생성자 주입을 통해 service 인스턴스를 초기화
    public ProductController(ProductService productService, CategoryService categoryService, ReviewService reviewService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.reviewService = reviewService;
    }

    // 메인 페이지 (전체 상품) - 상품 목록을 조회하고 정렬 옵션을 처리
    @GetMapping({"/products", "/products"})
    @Operation(summary = "상품 목록 조회", description = "모든 상품을 조회합니다.")
    public String listProducts(@RequestParam(defaultValue = "newest") String sort, Model model) {
        // 상품을 정렬 기준에 따라 리스트업
        List<Product> products = productService.listAllProductsSorted(sort);
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.findAllCategories());  // 카테고리 목록 추가
        return "/product/index2";  // 상품 목록 페이지로 이동
    }

    // 상품 등록 폼을 제공하는 페이지
    @GetMapping("/products/add")
    public String addProductForm(Model model) {
        model.addAttribute("categories", categoryService.findAllCategories());  // 카테고리 목록 추가
        model.addAttribute("subcategories", categoryService.findAllSubcategories());  // 서브카테고리 목록 추가
        model.addAttribute("product", new Product());  // 새로운 상품 객체 추가
        return "/product/addProduct";  // 상품 등록 폼 페이지로 이동
    }

    // 상품 등록 처리
    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute("product") Product product,
                             @RequestParam("categoryId") Long categoryId,
                             @RequestParam("subcategoryId") Long subcategoryId,
                             @RequestParam("imageFile") MultipartFile file) {
        System.out.println("요청 도착");
        // 업로드한 파일을 저장하고 파일 경로를 반환
        String imageUrl = uploadFile(file);
        product.setImageUrl(imageUrl);  // 상품 이미지 URL 설정
        productService.saveProduct(product, categoryId, subcategoryId);  // 상품 저장
        return "redirect:/products";  // 상품 목록 페이지로 리다이렉트
    }

    // 파일 업로드 처리
    private String uploadFile(MultipartFile file) {
        if (!file.isEmpty()) {
            // 파일을 저장할 기본 경로 설정
            String basePath = new File("src/main/resources/static/images").getAbsolutePath();
            String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            String filePath = basePath + File.separator + fileName;
            File destinationFile = new File(filePath);
            try {
                file.transferTo(destinationFile);  // 파일을 지정된 경로에 저장
                return "/images/" + fileName;  // 이미지 URL 반환
            } catch (IOException e) {
                System.out.println("Error during file upload: " + e.getMessage());  // 파일 업로드 에러 처리
                return null;  // 업로드 실패 시 null 반환
            }
        }
        return null;  // 파일이 없으면 null 반환
    }

    // 상품 상세 조회 페이지
    @GetMapping("/products/{id}")
    @Operation(summary = "상품 상세 조회", description = "상품 ID를 기반으로 상품 상세 정보를 조회합니다.")
    public String viewProduct(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);  // 상품 ID로 상품 정보 조회
        List<Review> reviews = reviewService.getReviewsByProductId(id);  // 상품 리뷰 조회
        model.addAttribute("product", product);  // 상품 정보 모델에 추가
        model.addAttribute("reviews", reviews);  // 상품 리뷰 모델에 추가
        return "/product/productDetail";  // 상품 상세 페이지로 이동
    }

    // 상품 수정 폼
    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        var product = productService.getProductById(id);  // 상품 ID로 상품 정보 조회
        model.addAttribute("product", product);  // 수정할 상품 정보 모델에 추가
        model.addAttribute("categories", categoryService.findAllCategories());  // 카테고리 목록 추가
        model.addAttribute("subcategories", categoryService.findAllSubcategories());  // 서브카테고리 목록 추가
        return "/product/editProduct";  // 상품 수정 폼 페이지로 이동
    }

    // 상품 수정 처리
    @PostMapping("/products/edit/{id}")
    public String editProduct(@PathVariable(name = "id") Long id,
                              @ModelAttribute("product") Product product,
                              @RequestParam(name = "categoryId") Long categoryId,
                              @RequestParam(name = "subcategoryId") Long subcategoryId,
                              @RequestParam(name = "imageFile") MultipartFile file) {
        // 업로드한 파일을 저장하고 파일 경로를 반환
        String imageUrl = uploadFile(file);
        product.setImageUrl(imageUrl);  // 새로운 이미지 URL을 상품 정보에 설정
        productService.updateProduct(id, product, categoryId, subcategoryId);  // 상품 수정 처리
        return "redirect:/products";  // 상품 목록 페이지로 리다이렉트
    }

    // 상품 삭제 처리
    @DeleteMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);  // 상품 삭제 처리
        return "redirect:/products";  // 상품 목록 페이지로 리다이렉트
    }

    // 카테고리별 상품 조회
    @GetMapping("/products/category/{categoryId}")
    public String getProductsByCategory(@PathVariable("categoryId") Long categoryId,
                                        @RequestParam(required = false, defaultValue = "newest") String sort,
                                        Model model) {
        Category category = categoryService.findCategoryById(categoryId);  // 카테고리 정보 조회
        List<Product> products = productService.findProductsByCategory(category, sort);  // 카테고리에 해당하는 상품 조회
        List<Subcategory> subcategories = categoryService.findSubcategoriesByCategory(category);  // 해당 카테고리의 서브카테고리 조회

        model.addAttribute("currentCategory", category);  // 현재 카테고리 모델에 추가
        model.addAttribute("products", products);  // 조회된 상품 리스트 모델에 추가
        model.addAttribute("subcategories", subcategories);  // 서브카테고리 리스트 모델에 추가
        model.addAttribute("categories", categoryService.findAllCategories());  // 전체 카테고리 리스트 추가
        model.addAttribute("sort", sort);  // 정렬 기준 유지
        System.out.println("Sort parameter received: " + sort);  // 정렬 기준 확인 로그 출력
        return "/product/index2";  // 상품 목록 페이지로 이동
    }

    // 카테고리별 상품 조회 JSON
    @GetMapping("/products/category/{categoryId}/json")
    public ResponseEntity<List<Product>> getProductsByCategoryJson(
            @PathVariable("categoryId") Long categoryId,
            @RequestParam(defaultValue = "newest") String sort) {
        Category category = categoryService.findCategoryById(categoryId);  // 카테고리 정보 조회
        List<Product> products = productService.findProductsByCategory(category, sort);  // 카테고리별 상품 조회
        return ResponseEntity.ok(products);  // 상품 리스트를 JSON 형식으로 반환
    }

    // 서브카테고리별 상품 조회
    @GetMapping("/products/subcategory/{subId}")
    public String getProductsBySubcategory(
            @PathVariable("subId") Long subId,
            @RequestParam(required = false, defaultValue = "newest") String sort,
            Model model) {

        Subcategory subcategory = categoryService.findSubcategoryById(subId);  // 서브카테고리 정보 조회
        List<Product> products = productService.findProductsBySubcategory(subcategory, sort);  // 서브카테고리별 상품 조회
        Category parentCategory = subcategory.getCategory();  // 해당 서브카테고리의 부모 카테고리 조회
        List<Subcategory> subcategories = categoryService.findSubcategoriesByCategory(parentCategory);  // 부모 카테고리의 서브카테고리 조회

        model.addAttribute("currentCategory", parentCategory);  // 부모 카테고리 모델에 추가
        model.addAttribute("products", products);  // 조회된 상품 리스트 모델에 추가
        model.addAttribute("subcategories", subcategories);  // 서브카테고리 리스트 모델에 추가
        model.addAttribute("categories", categoryService.findAllCategories());  // 전체 카테고리 리스트 추가
        model.addAttribute("sort", sort);  // 정렬 기준 유지

        return "/product/index2";  // 상품 목록 페이지로 이동
    }

    // 상품 검색 (상품 이름으로)
    @GetMapping("/products/search")
    public String searchProducts(@RequestParam("search") String search, Model model) {
        List<Product> products = productService.searchProducts(search);  // 상품 검색 처리
        model.addAttribute("products", products);  // 검색된 상품 목록 모델에 추가
        return "/product/index2";  // 검색 결과 페이지로 이동
    }

    // 상품 리뷰 등록
    @PostMapping("/products/{productId}/reviews")
    public String addReview(@PathVariable Long productId, @ModelAttribute Review review) {
        review.setProductId(productId);  // 리뷰에 상품 ID 설정
        // 테스트 목적 고정 userId
        review.setUserId(1L);  // 고정된 사용자 ID 설정
        reviewService.saveReview(review);  // 리뷰 저장
        return "redirect:/products/" + productId;  // 상품 상세 페이지로 리다이렉트
    }

    // 상품 리뷰 조회 API
    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<Review>> getReviews(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getReviewsByProductId(productId);  // 상품 리뷰 조회
        return ResponseEntity.ok(reviews);  // 리뷰를 JSON 형식으로 반환
    }

    // 리뷰 로그인 사용자 ID 반환 
    // private Long getUserIdFromPrincipal(Principal principal) {}
}

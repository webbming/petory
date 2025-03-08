package com.shoppingmall.product.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingmall.product.model.Category;
import com.shoppingmall.product.model.PetType;
import com.shoppingmall.product.model.Product;
import com.shoppingmall.product.model.Review;
import com.shoppingmall.product.model.Subcategory;
import com.shoppingmall.product.repository.ReviewRepository;
import com.shoppingmall.product.service.CategoryService;
import com.shoppingmall.product.service.ProductService;
import com.shoppingmall.product.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    // 생성자 주입을 통해 service 인스턴스를 초기화
    public ProductController(ProductService productService, CategoryService categoryService, ReviewService reviewService, ReviewRepository reviewRepository) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.reviewService = reviewService;
        this.reviewRepository = reviewRepository;
    }

    // 메인 페이지 (전체 상품) - 상품 목록을 조회하고 정렬 옵션을 처리
    @GetMapping("/products")
    @Operation(summary = "상품 목록 조회", description = "모든 상품을 조회하거나 petType으로 필터링합니다.")
    public String listProducts(@RequestParam(defaultValue = "newest") String sort,
                               @RequestParam(required = false) PetType petType, Model model) {
        List<Product> products;

        if (petType != null) {
            products = productService.getProductsByPetType(petType); // 🐱🐶 petType 필터링 적용
        } else {
            products = productService.listAllProductsSorted(sort);
        }

        // 각 상품의 리뷰 개수를 동적으로 계산
        for (Product product : products) {
            int reviewCount = reviewService.getReviewsByProductId(product.getProductId()).size();
            product.setReviewCount(reviewCount);
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("selectedPetType", petType); // 선택된 petType 유지
        return "/product/index2";
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
                             @RequestParam("petType") PetType petType,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             @RequestParam("detailImageFiles") List<MultipartFile> detailImageFiles) {
        // 1. 대표 이미지 업로드 후 URL 반환
        String imageUrl = uploadFile(imageFile);
        if (imageUrl != null) {
            product.setImageUrl(imageUrl); // product에 imageUrl 저장
        }

        // 2. 상세 이미지 업로드
        List<String> detailImageUrls = uploadFiles(detailImageFiles);
        
     //  petType 설정
        product.setPetType(petType);

        // 3. 상품 저장 (이제 대표 이미지가 포함됨)
        productService.saveProduct(product, categoryId, subcategoryId, detailImageUrls);

        return "redirect:/products";
    }


    // 단일 파일 업로드 메소드
    private String uploadFile(MultipartFile file) {
        if (!file.isEmpty()) {
            String basePath = new File("src/main/resources/static/images").getAbsolutePath();
            String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            String filePath = basePath + File.separator + fileName;
            File destinationFile = new File(filePath);
            
            // ✅ 저장된 파일 경로 확인 (디버깅)
            System.out.println("저장된 대표 이미지 경로: " + filePath);

            try {
                file.transferTo(destinationFile);
                return "/images/" + fileName;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }


    // 여러 파일 업로드 메소드
    private List<String> uploadFiles(List<MultipartFile> files) {
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String imageUrl = uploadFile(file);
            if (imageUrl != null) {
                imageUrls.add(imageUrl);
            }
        }
        return imageUrls;
    }


    // 상품 상세 조회 페이지
    @GetMapping("/products/{id}")
    public String viewProduct(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        List<Review> reviews = reviewService.getReviewsByProductId(id);
        
        // ✅ 리뷰별 댓글 저장하는 Map 추가
        Map<Long, List<Map<String, Object>>> reviewComments = new HashMap<>();
        
        for (Review review : reviews) {
            try {
                reviewComments.put(review.getReviewId(), reviewService.getComments(review.getReviewId()));
            } catch (JsonProcessingException e) {
                System.err.println("JSON 파싱 오류: " + e.getMessage());
                reviewComments.put(review.getReviewId(), List.of()); // 예외 발생 시 빈 리스트 추가
            }
        }

        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewComments", reviewComments); // ✅ 댓글 데이터를 Thymeleaf에 전달

        return "/product/productDetail";
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
    public String editProduct(@PathVariable Long id,
                              @ModelAttribute("product") Product product,
                              @RequestParam("categoryId") Long categoryId,
                              @RequestParam("subcategoryId") Long subcategoryId,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              @RequestParam(value = "detailImageFiles", required = false) List<MultipartFile> detailImageFiles) {

        // 기존 상품 정보 가져오기
        Product existingProduct = productService.getProductById(id);

        // 기존 데이터 유지
        product.setProductId(existingProduct.getProductId());
        product.setCategory(categoryService.findCategoryById(categoryId));
        product.setSubcategory(categoryService.findSubcategoryById(subcategoryId));

        // 대표 이미지 업데이트 (새로운 이미지가 업로드된 경우에만 변경)
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = uploadFile(imageFile);
            if (imageUrl != null) {
                product.setImageUrl(imageUrl);
            }
        } else {
            product.setImageUrl(existingProduct.getImageUrl()); // 기존 이미지 유지
        }

        // 상세 이미지 업데이트
        List<String> detailImageUrls = (detailImageFiles != null && !detailImageFiles.isEmpty()) ?
                uploadFiles(detailImageFiles) : existingProduct.getDetailImageUrls();
        product.setDetailImageUrls(detailImageUrls);

        productService.updateProduct(id, product, categoryId, subcategoryId, detailImageUrls);
        
        return "redirect:/products/" + id;
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
                                        @RequestParam(required = false) PetType petType,
                                        @RequestParam(required = false, defaultValue = "newest") String sort,
                                        Model model) {
        Category category = categoryService.findCategoryById(categoryId);
        List<Product> products;

        if (petType != null) {
            products = productService.getProductsByCategoryAndPetType(categoryId, petType);
        } else {
            products = productService.findProductsByCategory(category, sort);
        }

        List<Subcategory> subcategories = categoryService.findSubcategoriesByCategory(category);

        model.addAttribute("currentCategory", category);
        model.addAttribute("products", products);
        model.addAttribute("subcategories", subcategories);
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("selectedPetType", petType); // 선택된 petType 유지
        return "/product/index2";
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
    public String getProductsBySubcategory(@PathVariable("subId") Long subId,
                                           @RequestParam(required = false) PetType petType,
                                           @RequestParam(required = false, defaultValue = "newest") String sort,
                                           Model model) {
        Subcategory subcategory = categoryService.findSubcategoryById(subId);
        List<Product> products;

        if (petType != null) {
            products = productService.getProductsBySubcategoryAndPetType(subId, petType);
        } else {
            products = productService.findProductsBySubcategory(subcategory, sort);
        }

        Category parentCategory = subcategory.getCategory();
        List<Subcategory> subcategories = categoryService.findSubcategoriesByCategory(parentCategory);

        model.addAttribute("currentCategory", parentCategory);
        model.addAttribute("products", products);
        model.addAttribute("subcategories", subcategories);
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("selectedPetType", petType); // 선택된 petType 유지
        return "/product/index2";
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
    public String addReview(@PathVariable Long productId, @ModelAttribute Review review, @RequestParam("imageFile") MultipartFile imageFile) {
        review.setProductId(productId);
        review.setUserId(1L); // 고정된 사용자 ID 설정
        reviewService.saveReview(review, imageFile); // 리뷰와 이미지 파일 저장
        return "redirect:/products/" + productId;
    }


    // 상품 리뷰 조회 API
    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<Review>> getReviews(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getReviewsByProductId(productId);  // 상품 리뷰 조회
        return ResponseEntity.ok(reviews);  // 리뷰를 JSON 형식으로 반환
    }
    
    @PostMapping("/products/{productId}/reviews/{reviewId}/comments")
    public String addCommentToReview(@PathVariable Long productId,
                                                @PathVariable Long reviewId,
                                                @RequestParam("content") String content,
                                                @RequestParam("userId") Long userId) {
        try {
            reviewService.addComment(reviewId, content, userId);
            return "redirect:/products/" + productId;  // 상품 상세 페이지로 리다이렉트
        } catch (Exception e) {
        	return "redirect:/products/" + productId;  // 에러 시에도 리다이렉트
        }
    }


    @GetMapping("/{productId}/reviews/{reviewId}/comments")
    public List<Map<String, Object>> getComments(Long reviewId) throws JsonProcessingException {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review == null || review.getComments() == null || review.getComments().isEmpty()) {
            return new ArrayList<>();  // 댓글 데이터가 없을 경우 빈 리스트 반환
        }
        return new ObjectMapper().readValue(review.getComments(), new TypeReference<List<Map<String, Object>>>() {});
    }


    // 리뷰 로그인 사용자 ID 반환 
    // private Long getUserIdFromPrincipal(Principal principal) {}
}

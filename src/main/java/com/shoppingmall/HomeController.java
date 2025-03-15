package com.shoppingmall;

import com.shoppingmall.board.dto.BoardResponseDTO;
import com.shoppingmall.search.dto.SearchResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "메인 홈페이지 API", description = "컨트롤러에 대한 설명입니다.")
@Controller
public class HomeController {

  private final HomeService homeService;

  public HomeController(HomeService homeService) {
    this.homeService = homeService;
  }

  @GetMapping("/")
  public String home() {
    return "home";
  }

  @GetMapping("/search")
  public String search(@RequestParam String keyword, Model model) {
    SearchResponseDTO searchResponseDTO =  homeService.search(keyword);

    model.addAttribute("boardList", searchResponseDTO.getBoardList());
    model.addAttribute("productList", searchResponseDTO.getProductList());
    model.addAttribute("keyword", keyword);
    return "search-result";
  }





}

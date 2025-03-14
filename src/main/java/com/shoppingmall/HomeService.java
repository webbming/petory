package com.shoppingmall;


import com.shoppingmall.board.dto.BoardResponseDTO;
import com.shoppingmall.board.model.Board;
import com.shoppingmall.board.repository.BoardRepository;
import com.shoppingmall.board.service.BoardService;
import com.shoppingmall.product.dto.ProductResponseDTO;
import com.shoppingmall.product.repository.ProductRepository;
import com.shoppingmall.product.service.ProductService;
import com.shoppingmall.search.dto.SearchResponseDTO;
import java.awt.print.Pageable;
import java.util.List;
import org.hibernate.query.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class HomeService {

  private final BoardRepository boardRepository;
  private final ProductRepository productRepository;
  private BoardService boardService;
  private ProductService productService;

  public HomeService(BoardService boardService, ProductService productService,
      BoardRepository boardRepository, ProductRepository productRepository) {
    this.boardService = boardService;
    this.productService = productService;
    this.boardRepository = boardRepository;
    this.productRepository = productRepository;
  }

  public SearchResponseDTO search(String keyword) {

    List<BoardResponseDTO> boardList = boardRepository.searchByKeyword(keyword)
        .stream().map(Board :: toDTO).toList();
    List<ProductResponseDTO> productList = productRepository.searchByKeyword(keyword)
        .stream().map(ProductResponseDTO :: toDTO).toList();

    SearchResponseDTO searchResponseDTO = new SearchResponseDTO();
    searchResponseDTO.setBoardList(boardList);
    searchResponseDTO.setProductList(productList);

    return searchResponseDTO;

  }


}

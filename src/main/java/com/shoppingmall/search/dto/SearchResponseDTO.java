package com.shoppingmall.search.dto;


import com.shoppingmall.board.dto.BoardResponseDTO;
import com.shoppingmall.product.dto.ProductResponseDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponseDTO {

  private List<BoardResponseDTO> boardList;
  private List<ProductResponseDTO> productList;

}

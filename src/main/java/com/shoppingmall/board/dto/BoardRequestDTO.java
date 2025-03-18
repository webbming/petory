package com.shoppingmall.board.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class BoardRequestDTO {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Likes{
    private String boardId;

    public Long getIdAsLong(String boardId) {
      return Long.parseLong(boardId);
    }
  }
}

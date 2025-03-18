package com.shoppingmall.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class commentRequestDTO {

	@Getter
	  @Setter
	  @NoArgsConstructor
	  @AllArgsConstructor
	  public static class Likes{
	    private String commentId;

	    public Long getIdAsLong(String commentId) {
	      return Long.parseLong(commentId);
	    }


	  }
}

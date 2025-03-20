package com.shoppingmall.board.dto;

import java.util.Set;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponseDTO {

    private Long boardId;
    private String title;
    private String categoryId;
    private String nickname;
    private String image;
    private String content;
    private Set<String> hashtag;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private String createAt;


}

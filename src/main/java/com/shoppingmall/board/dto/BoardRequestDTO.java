package com.shoppingmall.board.dto;


import com.shoppingmall.board.model.Board;
import com.shoppingmall.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BoardRequestDTO {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Write {
    private String categoryId;
    private String title;
    private String content;
    private String hashtags;
    private String image;
    private String postType;

    public Board toEntity(User user, PostType postType) {
      return Board.builder()
              .title(title)
              .user(user)
              .nickname(user.getNickname())
              .categoryId(categoryId)
              .content(content)
              .postType(postType)
              .commentCount(0)
              .likeCount(0)
              .viewCount(0)
              .image(imageExtractor(content))
              .likeContain(new HashSet<>())
              .viewContain(new HashSet<>())
              .hashtag(extractAndSaveHashtags(hashtags))
              .build();
    }
  }
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Update{
    private long boardId;
    private String title;
    private String categoryId;
    private String content;
    private String hashtag;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Likes{
    private String boardId;
    private String userId;

    public Long getIdAsLong(String boardId) {
      return Long.parseLong(boardId);
    }
    
    public Long getUserIdAsLong(String userId) {
    	return Long.parseLong(userId);
    }
  }
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Search{
    private int page;
    private int size;
    private String sort;
    private String search;
    private String period;
    private PostType postType;
    private String categoryId;
  }


    public static String imageExtractor(String content) {

      String regex = "<img\\s+src=\"([^\"]+)\"";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(content);

      String src = null;
      while (matcher.find()) {
        src = matcher.group(1);
        System.out.println("Extracted src: " + src);

      }
      return src;
    }

    public static Set<String> extractAndSaveHashtags(String hashtagsInput) {
      Set<String> hashtags = new HashSet<>();
      String[] hashtagArray = hashtagsInput.split("(?=#)");
      String tagName;
      for (String tag : hashtagArray) {
        tag = tag.replaceAll("[^#\\w\\p{IsHangul}]", "");
        tagName = tag.toLowerCase();
        hashtags.add(tagName);
      }
      return hashtags;
    }
}


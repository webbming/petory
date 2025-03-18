package com.shoppingmall.board.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.shoppingmall.board.dto.BoardResponseDTO;
import com.shoppingmall.board.model.Comment;
import com.shoppingmall.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shoppingmall.board.model.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{
	//키워드 검색
	@Query("SELECT b FROM Board b WHERE " +
		       "(LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
		       "OR LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
		       "OR LOWER(b.user.userId) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
		       "AND (:category = '' OR b.categoryId = :category) " +
		       "AND (:bydate = '전체' OR b.createdAt >= :startDate) " +
		       "AND (:hashtag = 'all' OR LOWER(b.hashtag) LIKE LOWER(CONCAT('%', :hashtag, '%'))) " +  // 💡 문법 오류 수정됨!
		       "ORDER BY CASE WHEN :order = '인기순' THEN b.likeCount ELSE 0 END DESC, " +
		       "b.createdAt DESC")
	List<Board> searchBoards(@Param("keyword") String keyword, @Param("category") String category, 
			@Param("order") String order, @Param("bydate") String bydate, @Param("startDate") LocalDateTime startDate,
			@Param("hashtag") String hashtag);
  // 전체 최신순 정렬
  Page<Board> findAllByOrderByBoardIdDesc(Pageable pageable);

  // 키워드 검색
  @Query(
      "SELECT b FROM Board b WHERE "
          + "(LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) "
          + "OR LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%')) "
          + "OR LOWER(b.user.userId) LIKE LOWER(CONCAT('%', :keyword, '%'))) "
          + "AND (:category = '' OR b.categoryId = :category) "
          + "AND (:bydate = '전체' OR b.createdAt >= :startDate) ORDER BY"
          + "  CASE WHEN :orderby = '인기순' THEN b.likeCount ELSE 0 END DESC, "
          + "  b.createdAt DESC")
  Page<Board> searchBoards(
      @Param("keyword") String keyword,
      @Param("category") String category,
      @Param("orderby") String orderby,
      @Param("bydate") String bydate,
      @Param("startDate") LocalDateTime startDate,
      Pageable pageable);

  List<Board> findTop9ByOrderByCreatedAtDesc();

  List<Board> findTop9ByOrderByLikeCountDesc();

  @Query("SELECT b FROM Board b ORDER BY b.likeCount DESC")
  Page<Board> findAllSortedByLikes(Pageable pageable);

  @Query("SELECT b FROM Board b WHERE b.title LIKE %:keyword% OR b.content LIKE %:keyword%")
  List<Board> searchByKeyword(String keyword);

  Page<Board> findByCategoryId(String category, Pageable pageable);


	Page<Board> findByCategoryIdAndCreatedAtAfter(String categoryId, LocalDateTime startDate, Pageable pageable);


	Page<Board> findByCreatedAtAfter(LocalDateTime startDate, Pageable pageable);


	Page<Board> findByCreatedAtAfterAndTitleContaining(
			LocalDateTime startDate, String search, Pageable pageable);

	Page<Board> findByCategoryIdAndCreatedAtAfterAndTitleContaining(
			String categoryId, LocalDateTime startDate, String search, Pageable pageable);

    List<Board> findTop9ByOrderByViewCountDesc();

    Page<Board> findBoardByUser(User user, Pageable pageable);

    int countByUser(User user);

    // 사용자가 댓글 단 게시물 수 조회
    @Query("SELECT COUNT(DISTINCT c.board) FROM Comment c WHERE c.user = :user")
    int countDistinctBoardsByCommentUser(@Param("user") User user);

    // 좋아요 수 조회는 네이티브 쿼리로 처리
    @Query(value = "SELECT COUNT(*) FROM board WHERE like_contain LIKE %:userId%", nativeQuery = true)
    int countBoardsByUserLikes(@Param("userId") Long userId);


}

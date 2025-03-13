package com.shoppingmall.board.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shoppingmall.board.model.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{
	//전체 최신순 정렬
	Page<Board> findAllByOrderByBoardIdDesc(Pageable pageable);
	
	//키워드 검색
	@Query("SELECT b FROM Board b WHERE " +
		       "(LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
		       "OR LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
		       "OR LOWER(b.user.userId) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
		       "AND (:category = '' OR b.categoryId = :category) " +
		       "AND (:bydate = '전체' OR b.createdAt >= :startDate) " +
		       "ORDER BY CASE WHEN :order = '인기순' THEN b.likeCount ELSE 0 END DESC, " +
		       "b.createdAt DESC")
	List<Board> searchBoards(@Param("keyword") String keyword, @Param("category") String category, 
			@Param("order") String order, @Param("bydate") String bydate, @Param("startDate") LocalDateTime startDate);
}

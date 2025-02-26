package com.shoppingmall.board.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "comment")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long commentId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id", referencedColumnName = "boardId", nullable = false)
	private Board board;
	
	private String userId;
	
	private String nickname;
	
	private String content;
	
	private int likeCount = 0;
	
	@Column(length = 1000)
	private List<Long> likeContain = new ArrayList<Long>();
	
	@CreationTimestamp
    @Column(updatable = false)
	private LocalDateTime createdAt;
}

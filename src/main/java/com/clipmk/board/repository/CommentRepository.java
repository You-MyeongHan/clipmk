package com.clipmk.board.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.clipmk.board.entity.Comment;
import com.clipmk.board.entity.Post;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	List<Comment> findByPostId(Long postId);
	Page<Comment> findByPostId(Long postId, Pageable pageable);
	List<Comment> findByPostIdAndParentIsNull(Long postId);
//	Long countByUser_Id(Integer userId);
	Page<Comment> findTop10ByUser_IdOrderByIdDesc(Integer userId, Pageable pageable);
	Page<Comment> findByUserId(int userId, Pageable pageable);
}
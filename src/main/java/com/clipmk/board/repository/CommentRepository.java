package com.clipmk.board.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.clipmk.board.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	@Query("SELECT c FROM Comment c WHERE c.post.postId = :postId")
	List<Comment> findByPostId(@Param("postId") Long postId);

	@Query("SELECT c FROM Comment c WHERE c.post.postId = :postId")
	Page<Comment> findByPostId(@Param("postId") Long postId, Pageable pageable);

	@Query("SELECT c FROM Comment c WHERE c.post.postId = :postId AND c.parent IS NULL")
	List<Comment> findByPostIdAndParentIsNull(@Param("postId") Long postId);

	@Query("SELECT c FROM Comment c WHERE c.user.id = :userId ORDER BY c.commentId DESC")
	Page<Comment> findTop10ByUser_IdOrderByIdDesc(@Param("userId") Integer userId, Pageable pageable);

	@Query("SELECT c FROM Comment c WHERE c.user.id = :userId")
	Page<Comment> findByUserId(@Param("userId") int userId, Pageable pageable);
}
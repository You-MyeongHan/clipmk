package com.bayclip.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bayclip.board.entity.Board;
import com.bayclip.board.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	List<Comment> findByBoard(Board board);
}
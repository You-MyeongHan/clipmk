package com.bayclip.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bayclip.board.entity.ReplyComment;

public interface ReplyCommentRepository extends JpaRepository<ReplyComment, Long>{

}

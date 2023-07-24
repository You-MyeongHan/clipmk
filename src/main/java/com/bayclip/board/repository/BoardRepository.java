package com.bayclip.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.bayclip.board.entity.Post;

public interface BoardRepository extends JpaRepository<Post, Long>,JpaSpecificationExecutor<Post>{
	@EntityGraph(value = "Post.userWithNick", type = EntityGraph.EntityGraphType.FETCH)
	Post findWithUserNickById(Long postId);
	Page<Post> findByViewCntGreaterThan(Integer viewCount, Pageable pageable);
	Page<Post> findByTitleContaining(Pageable pageable, String searchKeyword);
	Long countByUser_Id(Integer userId);
	Page<Post> findTop10ByUser_IdOrderByIdDesc(Integer userId, Pageable pageable);
}
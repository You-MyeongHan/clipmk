package com.bayclip.board.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.bayclip.board.entity.Post;
import com.bayclip.user.entity.User;

public interface BoardRepository extends JpaRepository<Post, Long>,JpaSpecificationExecutor<Post>{
	Optional<Post> findById(Long postId);
	Page<Post> findByViewCntGreaterThan(Integer viewCount, Pageable pageable);
	Page<Post> findByTitleContaining(Pageable pageable, String searchTerm);
	Long countByUser_Id(Integer userId);
	Page<Post> findTop10ByUser_IdOrderByIdDesc(Integer userId, Pageable pageable);
}
package com.bayclip.board.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.bayclip.board.entity.Post;

public interface BoardRepository extends JpaRepository<Post, Long>,JpaSpecificationExecutor<Post>{
	public Optional<Post> findById(Long postId);
	public Page<Post> findByViewCntGreaterThan(Integer viewCount, Pageable pageable);
	public Page<Post> findByTitleContaining(Pageable pageable, String searchTerm);
	public Long countByUser_Id(Integer userId);
	public Page<Post> findTop10ByUser_IdOrderByIdDesc(Integer userId, Pageable pageable);
}
package com.clipmk.board.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.clipmk.board.dto.PostsResDto;
import com.clipmk.board.entity.Post;
import com.infra.meta.dto.PostIdDto;

public interface BoardRepository extends JpaRepository<Post, Long>,JpaSpecificationExecutor<Post>{
	@Query("""
    SELECT new com.clipmk.board.dto.PostsResDto(
        P.postId,
        P.title,
        P.category,
        P.subCategory,
        U.nick,
        P.frsRgDtm,
        P.viewCnt,
        0,
        0,
        P.thumbnail
    )
    FROM Post P
    JOIN P.user U
    ORDER BY P.postId DESC
    """)
    Page<PostsResDto> findPosts(Pageable pageable);


	Optional<Post> findByPostId(Long postId);
	Page<Post> findByViewCntGreaterThan(Integer viewCount, Pageable pageable);
	Page<Post> findByTitleContaining(Pageable pageable, String searchTerm);
	Long countByUserId(Integer userId);
	Page<Post> findTop10ByUserIdOrderByPostIdDesc(Integer userId, Pageable pageable);
	Page<Post> findByUserId(int userId, Pageable pageable);
	@Query("SELECT new com.infra.meta.dto.PostIdDto(p.postId, p.frsRgDtm) FROM Post p")
	List<PostIdDto> findAllPostIds();
}
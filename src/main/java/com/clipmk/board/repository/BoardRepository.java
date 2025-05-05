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
        P.id,
        P.title,
        P.tbl,
        P.grp,
        U.nick,
        P.wr_date,
        P.viewCnt,
        0,
        0,
        P.thumbnail
    )
    FROM Post P
    JOIN P.user U
    ORDER BY P.id DESC
    """)
    Page<PostsResDto> findPosts(Pageable pageable);


	Optional<Post> findById(Long postId);
	Page<Post> findByViewCntGreaterThan(Integer viewCount, Pageable pageable);
	Page<Post> findByTitleContaining(Pageable pageable, String searchTerm);
	Long countByUser_Id(Integer userId);
	Page<Post> findTop10ByUser_IdOrderByIdDesc(Integer userId, Pageable pageable);
	Page<Post> findByUserId(int userId, Pageable pageable);
	@Query("SELECT new com.infra.meta.dto.PostIdDto(p.id, p.wr_date) FROM Post p")
	List<PostIdDto> findAllPostIds();
}
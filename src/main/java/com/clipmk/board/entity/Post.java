package com.clipmk.board.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.clipmk.board.dto.PostDto;
import com.clipmk.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="post")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String tbl;
    private String grp;

    @Column(length = 3000)
    private String content;

    @CreationTimestamp
    private LocalDateTime wr_date;
    private LocalDateTime re_date;
    private LocalDateTime del_date;

    @ColumnDefault("0")
    @Column(name = "view_cnt")
    private Integer viewCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    private String thumbnail;

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // PostReaction과 연관 관계
    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostReaction> reactions = new ArrayList<>();

    @Transactional
    public void updateViewCnt(Integer visit) {
        this.viewCnt = visit;
    }

    // 추천/비추천 개수 구하는 헬퍼 메서드
    public long getRecommendCount() {
        return reactions.stream()
                .filter(r -> r.getType() == PostReaction.ReactionType.UP)
                .count();
    }

    public long getDecommendCount() {
        return reactions.stream()
                .filter(r -> r.getType() == PostReaction.ReactionType.DOWN)
                .count();
    }

    public PostDto toDto() {
        return PostDto.builder()
                .id(this.id)
                .title(this.title)
                .tbl(this.tbl)
                .grp(this.grp)
                .content(this.content)
                .thumbnail(this.thumbnail)
                .wr_date(this.wr_date)
                .re_date(this.re_date)
                .del_date(this.del_date)
                .viewCnt(this.viewCnt)
                .user_nick(this.user.getNick())
                .user_id(this.user.getId())
                .recommend_cnt((int) this.getRecommendCount())
                .decommend_cnt((int) this.getDecommendCount())
                .build();
    }
}

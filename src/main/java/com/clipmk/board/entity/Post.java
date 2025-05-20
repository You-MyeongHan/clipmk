package com.clipmk.board.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
    @Column(name = "POST_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(name = "TITLE", nullable = false, length = 200)
    private String title;

    @Column(name = "CONTENT", length = 3000)
    private String content;

    @Column(name = "CATEGORY", nullable = false, length=5)
    private String category;

    @Column(name = "SUB_CATEGORY", nullable = false, length=5)
    private String subCategory;

    @Column(name = "FRS_RG_GUID", nullable = false, length = 36)
    private String firstRegisterGuid;  // 최초 등록자 GUID

    @Column(name = "LST_CH_GUID", nullable = false, length = 36)
    private String lastChangeGuid;  // 최종 변경자 GUID

    @CreationTimestamp
    @Column(name = "FRS_RG_DTM", nullable = false)
    private LocalDateTime frsRgDtm;

    @CreationTimestamp
    @Column(name = "LAST_CH_DTM", nullable = false)
    private LocalDateTime lastChDtm;

    @Column(name = "DEL_YN")
    private String delYn;

    @ColumnDefault("0")
    @Column(name = "VIEW_CNT")
    private Integer viewCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
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

    @PrePersist
    protected void onCreate() {
        // 최초 등록 시 GUID 생성
        if (firstRegisterGuid == null) {
            firstRegisterGuid = UUID.randomUUID().toString();
        }
        // 최종 변경자 GUID도 최초 등록자와 동일하게 설정
        if (lastChangeGuid == null) {
            lastChangeGuid = firstRegisterGuid;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        // 수정 시 최종 변경자 GUID 업데이트
        lastChangeGuid = UUID.randomUUID().toString();
    }

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

    // 추천/비추천 상태 확인 메서드
    public int getRecommendState(User user) {
        if (user == null) return 0;
        
        return reactions.stream()
                .filter(r -> r.getUser().getId().equals(user.getId()))
                .findFirst()
                .map(r -> r.getType() == PostReaction.ReactionType.UP ? 1 : -1)
                .orElse(0);
    }

    // 추천/비추천 추가/제거 메서드
    public void toggleReaction(User user, PostReaction.ReactionType type) {
        reactions.stream()
                .filter(r -> r.getUser().getId().equals(user.getId()))
                .findFirst()
                .ifPresentOrElse(
                    reaction -> {
                        if (reaction.getType() == type) {
                            // 같은 타입의 반응이면 제거
                            reactions.remove(reaction);
                        } else {
                            // 다른 타입의 반응이면 타입 변경
                            reaction.setType(type);
                        }
                    },
                    () -> {
                        // 새로운 반응 추가
                        PostReaction newReaction = PostReaction.builder()
                                .user(user)
                                .post(this)
                                .type(type)
                                .build();
                        reactions.add(newReaction);
                    }
                );
    }

    public PostDto toDto() {
        return PostDto.builder()
                .postId(this.postId)
                .title(this.title)
                .category(this.category)
                .subCategory(this.subCategory)
                .content(this.content)
                .thumbnail(this.thumbnail)
                .wrDate(this.frsRgDtm)
                .reDate(this.lastChDtm)
                .delDate(this.delYn != null ? LocalDateTime.now() : null)
                .viewCnt(this.viewCnt)
                .userNick(this.user.getNick())
                .userId(this.user.getId())
                .recommendCnt((int) this.getRecommendCount())
                .decommendCnt((int) this.getDecommendCount())
                .build();
    }
}

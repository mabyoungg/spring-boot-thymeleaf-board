package com.ll.likelionspringboottestmedium.domain.post.post.entity;

import com.ll.likelionspringboottestmedium.domain.memeber.memeber.entity.Member;
import com.ll.likelionspringboottestmedium.domain.post.postComment.entity.PostComment;
import com.ll.likelionspringboottestmedium.domain.post.postLike.entity.PostLike;
import com.ll.likelionspringboottestmedium.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
@Getter
@Setter
@ToString(callSuper = true)
public class Post extends BaseEntity {
    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<PostLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    @Builder.Default
    @OrderBy("id DESC")
    @ToString.Exclude
    private List<PostComment> comments = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    private Member author;
    private String title;

    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    private PostDetail detailBody;

    private boolean published;

    @Setter(PROTECTED)
    private long hit;

    @Setter(PROTECTED)
    private long likesCount;

    private int minMembershipLevel;


    public void increaseLikesCount() {
        likesCount++;
    }

    private void decreaseLikesCount() {
        likesCount--;
    }

    public void increaseHit() {
        hit++;
    }

    public void addLike(Member member) {
        if (hasLike(member)) {
            return;
        }

        likes.add(PostLike.builder()
                .post(this)
                .member(member)
                .build());

        increaseLikesCount();
    }

    public boolean hasLike(Member member) {
        return likes.stream()
                .anyMatch(postLike -> postLike.getMember().equals(member));
    }

    public void deleteLike(Member member) {
        boolean removed = likes.removeIf(postLike -> postLike.getMember().equals(member));

        if (removed) {
            decreaseLikesCount();
        }
    }

    public PostComment writeComment(Member actor, String body) {
        PostComment postComment = PostComment.builder()
                .post(this)
                .author(actor)
                .body(body)
                .build();

        comments.add(postComment);

        return postComment;
    }

    public String getBodyForEditor() {
        return getDetailBody()
                .getVal()
                .replaceAll("(?i)(</?)script", "$1t-script");
    }
}

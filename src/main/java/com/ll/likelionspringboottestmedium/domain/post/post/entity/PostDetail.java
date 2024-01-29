package com.ll.likelionspringboottestmedium.domain.post.post.entity;

import com.ll.likelionspringboottestmedium.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"post_id", "name"})
})
@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
@Getter
@Setter
@ToString(callSuper = true)
public class PostDetail extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Post post;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String val;
}

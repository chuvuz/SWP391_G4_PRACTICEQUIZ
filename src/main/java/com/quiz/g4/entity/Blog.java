package com.quiz.g4.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "blogs")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id")
    private Integer blogId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "image", nullable = true)
    private String image;

    @Column(name = "is_active", nullable = false)
    private Boolean isActiveBlog = true;

    // Tạo quan hệ với bảng User (Marketing role sẽ có thể tạo blog)
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;


}

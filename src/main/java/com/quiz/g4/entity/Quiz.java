package com.quiz.g4.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Integer quizId;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "quiz_name", nullable = false)
    private String quizName;

    @Column(name = "quiz_image")  // Trường bổ sung cho ảnh bài kiểm tra
    private String quizImage;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate = LocalDate.now();

    @OneToMany(mappedBy = "quiz")
    private Set<RecordStudent> records;
}

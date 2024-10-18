package com.quiz.g4.entity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(name = "quiz_name", nullable = false)
    private String quizName;

    @Column(name = "quiz_image")
    private String quizImage;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate = LocalDate.now();

    @Column(name = "updated_date")
    private LocalDate updatedDate = LocalDate.now();

    @Column(name = "is_active", nullable = false)
    @ColumnDefault("1")
    private Boolean isActive;


//    @OneToMany(mappedBy = "quiz")
//    private Set<Lesson> lessons;  // Liên kết với các bài học

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)  // Liên kết với bảng Subject
    private Subject subject;
}
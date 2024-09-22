package com.quiz.g4.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Integer lessonId;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "lesson_name", nullable = false)
    private String lessonName;

    @ManyToMany
    @JoinTable(
            name = "lesson_questions",
            joinColumns = @JoinColumn(name = "lesson_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private Set<QuestionBank> questionBanks;  // Liên kết nhiều-nhiều với câu hỏi trong ngân hàng
}

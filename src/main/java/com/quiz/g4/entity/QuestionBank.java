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
@Table(name = "question_bank")
public class QuestionBank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Integer questionId;

    @Column(name = "question_content", nullable = false)
    private String questionContent;

    @Column(name = "question_type", nullable = false)
    private String questionType;

    @Column(name = "subject_id")
    private Subject subject;

    @Column(name = "lesson_id")
    private Lesson lesson;

    @OneToMany(mappedBy = "questionBank")
    private Set<AnswerOption> answerOptions; // Liên kết với câu trả lời

//    @ManyToMany(mappedBy = "questionBanks")
//    private Set<Lesson> lessons; // Liên kết với các bài học (nhiều-nhiều)
}

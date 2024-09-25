package com.quiz.g4.entity;
import lombok.*;

import javax.persistence.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "answer_option")
public class AnswerOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Integer optionId;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionBank questionBank; // Thay đổi tên biến để phù hợp với mappedBy

    @Column(name = "option_content", nullable = false)
    private String content;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect; // Đánh dấu đáp án đúng
}

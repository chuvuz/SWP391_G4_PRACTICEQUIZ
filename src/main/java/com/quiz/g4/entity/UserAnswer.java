package com.quiz.g4.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user_answers")
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Integer answerId;

    @ManyToOne
    @JoinColumn(name = "result_id", nullable = false)
    private QuizResult quizResult;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "selected_option_ids")
    private String selectedOptionIds;  // Lưu trữ ID của các lựa chọn mà học sinh đã chọn

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;  // Đánh dấu đúng/sai của câu trả lời
}

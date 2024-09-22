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
@Table(name = "user_answers")
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Integer answerId;

    @ManyToOne
    @JoinColumn(name = "quiz_result_id", nullable = false)
    private QuizResult quizResult;  // Liên kết với kết quả quiz

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionBank question;

    @ManyToOne
    @JoinColumn(name = "selected_option_id", nullable = false)
    private AnswerOption selectedAnswer;  // Câu trả lời mà học sinh đã chọn

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;  // Xác định đúng/sai
}

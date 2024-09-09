package com.quiz.g4.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "answer_options")
public class AnswerOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Integer optionId;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "option_content", nullable = false)
    private String content;  // Nội dung lựa chọn trả lời

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;  // Xác định đây có phải đáp án đúng không
}

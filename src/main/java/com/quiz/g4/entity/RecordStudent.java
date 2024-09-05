package com.quiz.g4.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "record_student")
public class RecordStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Integer recordId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "score", nullable = false)
    private Float score; // Điểm số đã chuẩn hóa trên thang điểm 10

    @Column(name = "attempt_date", nullable = false)
    private LocalDateTime attemptDate = LocalDateTime.now();

    @OneToMany(mappedBy = "recordStudent")
    private Set<StudentAnswer> studentAnswers;
}

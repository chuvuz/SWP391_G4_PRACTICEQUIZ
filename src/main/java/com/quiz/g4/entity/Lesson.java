package com.quiz.g4.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Integer lessonId;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    @JsonBackReference
    private Subject subject;

    @Column(name = "lesson_name", nullable = false)
    private String lessonName;

    @Column(name = "created_date")
    @JsonIgnore // Bỏ qua trường này khi chuyển đổi thành JSON
    private LocalDate createdDate = LocalDate.now();

    @Column(name = "updated_date")
    @JsonIgnore // Bỏ qua trường này khi chuyển đổi thành JSON
    private LocalDate updatedDate = LocalDate.now();

    // One lesson can have many quizzes
    @OneToMany(mappedBy = "lesson")
    @JsonIgnore // Bỏ qua trường này khi chuyển đổi thành JSON
    private Set<Quiz> quizzes;

}

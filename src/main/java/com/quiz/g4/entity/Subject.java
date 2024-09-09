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
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Integer subjectId;

    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    @Column(name = "subject_image")  // Trường bổ sung cho ảnh môn học
    private String subjectImage;

    @OneToMany(mappedBy = "subject")
    private Set<User> users;

    @OneToMany(mappedBy = "subject")
    private Set<Quiz> quizzes;
}

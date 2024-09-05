package com.quiz.g4.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Integer subjectId;

    @Column(name = "subject_name", nullable = false, unique = true)
    private String subjectName;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "subject")
    private Set<User> users;

    @OneToMany(mappedBy = "subject")
    private Set<Quiz> quizzes;
}

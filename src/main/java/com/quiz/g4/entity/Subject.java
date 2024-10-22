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

    @Column(name = "subject_image")
    private String subjectImage;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "subject")
    private Set<Quiz> quizzes;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private Set<Lesson> lessons;
    // One subject contains one or more lessons

    @OneToMany(mappedBy = "subject")
    private Set<QuestionBank> questionBanks;
}




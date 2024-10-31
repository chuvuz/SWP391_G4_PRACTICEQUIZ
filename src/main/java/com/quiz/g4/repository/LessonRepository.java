package com.quiz.g4.repository;

import com.quiz.g4.entity.Lesson;
import com.quiz.g4.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson,Integer> {
//    @Query("SELECT l FROM Lesson l " +
//            "LEFT JOIN FETCH l.questionBanks qb " +
//            "LEFT JOIN FETCH qb.answerOptions " +
//            "WHERE l.lessonId = :lessonId")
//    Lesson findLessonWithQuestionsById(@Param("lessonId") Integer lessonId);
    Lesson findByLessonId(Integer lessonId);

    List<Lesson> findLessonsBySubject (Subject subject);
}

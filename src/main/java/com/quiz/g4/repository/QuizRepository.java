package com.quiz.g4.repository;

import com.quiz.g4.entity.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Integer> {
    @Query("SELECT q FROM Quiz q WHERE (:quizName IS NULL OR q.quizName LIKE %:quizName%)" +
            " AND (:subjectId IS NULL OR q.subject.subjectId = :subjectId)" +
            " AND (:expertId IS NULL OR q.createdBy.userId = :expertId)")
    Page<Quiz> searchQuizzes(String quizName, Integer subjectId, Integer expertId, Pageable pageable);



    @Query("SELECT DISTINCT q FROM Quiz q " +
            "JOIN FETCH q.questions qu " +
            "JOIN FETCH qu.answerOptions " +
            "WHERE q.quizId = :quizId")
    Quiz findQuizWithQuestionsAndAnswers(@Param("quizId") Integer quizId);

    @Query("SELECT q FROM Quiz q WHERE q.createdBy = :autherId")
    List<Quiz> findQuizByAuther(@Param("autherId") Integer autherId);

}

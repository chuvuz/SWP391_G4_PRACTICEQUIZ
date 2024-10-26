package com.quiz.g4.repository;

import com.quiz.g4.entity.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Integer> {

    @Query("SELECT q FROM Quiz q WHERE q.createdBy.userId = :autherId")
    List<Quiz> findQuizByAuther(@Param("autherId") Integer autherId);


    @Query("SELECT q FROM Quiz q WHERE (:quizName IS NULL OR q.quizName LIKE %:quizName%)" +
            " AND (:subjectId IS NULL OR q.subject.subjectId = :subjectId)" +
            " AND (:expertId IS NULL OR q.createdBy.userId = :expertId)" +
            " AND q.isActive = true")
    Page<Quiz> searchQuizzes(String quizName, Integer subjectId, Integer expertId, Pageable pageable);


    @Query("SELECT q FROM Quiz q WHERE q.isActive = true " +
            "ORDER BY q.createdDate desc ")
    List<Quiz> findQuizRecentAndIsActive();

//    @Query("SELECT q FROM Quiz q " +
//            "JOIN FETCH q.lessons l " +
//            "WHERE q.quizId = :quizId " +
//            "ORDER BY l.createdDate ASC")
//    Quiz findQuizWithLessons(@Param("quizId") Integer quizId);


    @Query("SELECT q FROM Quiz q WHERE q.isActive = true")
    Page<Quiz> findAllActiveQuizzes(Pageable pageable);

    @Query("SELECT q FROM Quiz q JOIN q.lesson l WHERE l.lessonId = :lessonId")
    List<Quiz> findQuizzesByLessonId(@Param("lessonId") Integer lessonId);

    @Query("SELECT q FROM Quiz q WHERE q.createdBy.userId = :authorId")
    Page<Quiz> findByAuthorId(@Param("authorId") Integer userId, Pageable pageable);

}

package com.quiz.g4.repository;

<<<<<<< Updated upstream
import com.quiz.g4.entity.Quiz;
=======
import com.quiz.g4.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
>>>>>>> Stashed changes
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
<<<<<<< Updated upstream
public interface QuizRepository extends JpaRepository<Quiz,Integer> {
=======
public interface QuizRepository extends JpaRepository<Subject,Integer> {
    @Query("SELECT q FROM Subject q WHERE (:quizName IS NULL OR q.quizName LIKE %:quizName%)" +
            " AND (:subjectId IS NULL OR q.subject.subjectId = :subjectId)" +
            " AND (:expertId IS NULL OR q.createdBy.userId = :expertId)")
    Page<Subject> searchQuizzes(String quizName, Integer subjectId, Integer expertId, Pageable pageable);



    @Query("SELECT DISTINCT q FROM Subject q " +
            "JOIN FETCH q.questions qu " +
            "JOIN FETCH qu.answerOptions " +
            "WHERE q.quizId = :quizId")
    Subject findQuizWithQuestionsAndAnswers(@Param("quizId") Integer quizId);

>>>>>>> Stashed changes
}

package com.quiz.g4.repository;

import com.quiz.g4.entity.UserAnswer;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Integer> {
    @Query("SELECT ua FROM UserAnswer ua WHERE ua.quizResult.resultId = :resultId")
    List<UserAnswer> findByQuizResultId(@Param("resultId") Integer resultId);
}

package com.quiz.g4.repository;

import com.quiz.g4.entity.AnswerOption;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Integer> {

    @Query("SELECT ao FROM AnswerOption ao WHERE ao.questionBank.questionId = :questionId")
    List<AnswerOption> findByQuestionId(@Param("questionId") Integer questionId);

    @Query("SELECT ao FROM AnswerOption ao WHERE ao.questionBank.questionId = :questionId AND ao.isCorrect = true")
    List<AnswerOption> findCorrectOptionsByQuestionId(@Param("questionId") Integer questionId);



    @Query("SELECT ao FROM AnswerOption ao WHERE ao.questionBank.questionId = :questionId AND ao.isCorrect = true")
    AnswerOption findCorrectOptionByQuestionId(@Param("questionId") Integer questionId);
}

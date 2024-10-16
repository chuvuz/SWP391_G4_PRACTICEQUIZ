package com.quiz.g4.repository;

import com.quiz.g4.entity.QuestionBank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionBankRepository extends JpaRepository<QuestionBank, Integer>, CrudRepository<QuestionBank, Integer> {

    @Query("SELECT qb FROM QuestionBank qb WHERE (:questionContent IS NULL OR qb.questionContent LIKE %:questionContent%)" +
            " AND (:questionType IS NULL OR qb.questionType = :questionType)")
    Page<QuestionBank> searchQuestion(String questionContent,
                                      String questionType,
                                      Pageable pages);

    boolean existsByQuestionContent(String questionContent);

    QuestionBank findByQuestionId(Integer id);

}

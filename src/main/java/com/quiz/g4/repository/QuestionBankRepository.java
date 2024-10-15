package com.quiz.g4.repository;

import com.quiz.g4.entity.QuestionBank;
import com.quiz.g4.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionBankRepository extends JpaRepository<QuestionBank, Integer> {

    @Query("SELECT qb FROM QuestionBank qb WHERE (:questionContent IS NULL OR qb.questionContent LIKE %:questionContent%)" +
            " AND (:questionType IS NULL OR qb.questionType = :questionType)")
    Page<QuestionBank> searchQuestion(String questionContent,
                                      String questionType,
                                      Pageable pages);

}

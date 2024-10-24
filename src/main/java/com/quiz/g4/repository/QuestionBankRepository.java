package com.quiz.g4.repository;

import com.quiz.g4.entity.Lesson;
import com.quiz.g4.entity.QuestionBank;
import com.quiz.g4.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionBankRepository extends JpaRepository<QuestionBank, Integer>, CrudRepository<QuestionBank, Integer> {

    @Query("SELECT qb FROM QuestionBank qb WHERE" +
            " (:questionContent IS NULL OR LOWER(qb.questionContent) LIKE LOWER(CONCAT('%', :questionContent, '%')))" +
            " AND (:questionType IS NULL OR qb.questionType = :questionType)" +
            " AND (:subject IS NULL OR qb.subject = :subject)" +
            " AND (:lesson IS NULL OR qb.lesson = :lesson)")
    Page<QuestionBank> searchQuestion(@Param("questionContent") String questionContent,
                                      @Param("questionType") String questionType,
                                      @Param("subject") Subject subject,
                                      @Param("lesson") Lesson lesson,
                                      Pageable pages);

    boolean existsByQuestionContent(String questionContent);

    QuestionBank findByQuestionId(Integer id);

}

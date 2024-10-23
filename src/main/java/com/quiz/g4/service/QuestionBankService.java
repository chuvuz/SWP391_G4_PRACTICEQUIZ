package com.quiz.g4.service;

import com.quiz.g4.entity.QuestionBank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface QuestionBankService {

/*
    QuestionBank createQuestion(QuestionRequest questionRequest);
*/
    QuestionBank findById(Integer id);

    boolean existsByQuestionContent(String questionContent);

    void save(QuestionBank question);

    Page<QuestionBank> allQuestions(Pageable pages);

    Page<QuestionBank> searchQuestion(Pageable pages, String questionContent, String questionType, Integer subject, Integer lesson);

    boolean checkCorrectAnswer(Integer questionId, Integer selectedOptionId);
}

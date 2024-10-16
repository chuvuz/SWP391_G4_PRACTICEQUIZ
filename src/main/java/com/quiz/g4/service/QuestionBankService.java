package com.quiz.g4.service;

import com.quiz.g4.entity.QuestionBank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionBankService {

/*
    QuestionBank createQuestion(QuestionRequest questionRequest);
*/
    void save(QuestionBank question);

    Page<QuestionBank> allQuestions(Pageable pages);

    Page<QuestionBank> searchQuestion(Pageable pages, String questionContent, String questionType);

    boolean checkCorrectAnswer(Integer questionId, Integer selectedOptionId);
}

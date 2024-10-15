package com.quiz.g4.service;

import com.quiz.g4.entity.QuestionBank;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionBankService {

    List<QuestionBank> allQuestions();

    boolean checkCorrectAnswer(Integer questionId, Integer selectedOptionId);
}

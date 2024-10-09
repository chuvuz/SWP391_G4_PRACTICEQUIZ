package com.quiz.g4.service;

import org.springframework.stereotype.Service;

@Service
public interface QuestionBankService {


    boolean checkCorrectAnswer(Integer questionId, Integer selectedOptionId);
}

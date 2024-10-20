package com.quiz.g4.service;

import com.quiz.g4.entity.QuizResult;
import org.springframework.stereotype.Service;

@Service
public interface QuizResultService {
    void saveQuizResult(QuizResult quizResult);

    QuizResult findQuizResultById(Integer resultId);
}

package com.quiz.g4.service.impl;

import com.quiz.g4.entity.QuizResult;
import com.quiz.g4.repository.QuizResultRepository;
import com.quiz.g4.service.QuizResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizResultServiceImpl implements QuizResultService {

    @Autowired
    private QuizResultRepository quizResultRepository;
    @Override
    public void saveQuizResult(QuizResult quizResult) {
        quizResultRepository.save(quizResult);
    }

    @Override
    public QuizResult findQuizResultById(Integer resultId) {
        return quizResultRepository.findById(resultId).orElseThrow(null);
    }
}

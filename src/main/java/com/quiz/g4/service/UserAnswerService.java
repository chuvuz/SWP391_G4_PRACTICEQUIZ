package com.quiz.g4.service;

import com.quiz.g4.entity.UserAnswer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserAnswerService {
    void saveUserAnswer(UserAnswer userAnswer);

    List<UserAnswer> findByQuizResultId(Integer resultId);
}

package com.quiz.g4.service.impl;

import com.quiz.g4.entity.UserAnswer;
import com.quiz.g4.repository.UserAnswerRepository;
import com.quiz.g4.service.UserAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAnswerServiceImpl implements UserAnswerService {

    @Autowired
    private UserAnswerRepository userAnswerRepository;


    @Override
    public void saveUserAnswer(UserAnswer userAnswer) {
        userAnswerRepository.save(userAnswer);
    }

    @Override
    public List<UserAnswer> findByQuizResultId(Integer resultId) {
        return userAnswerRepository.findByQuizResultId(resultId);
    }
}

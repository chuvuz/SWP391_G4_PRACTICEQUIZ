package com.quiz.g4.service.impl;

import com.quiz.g4.entity.AnswerOption;
import com.quiz.g4.repository.AnswerOptionRepository;
import com.quiz.g4.service.AnswerOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerOptionServiceImpl implements AnswerOptionService {

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @Override
    public AnswerOption findById(Integer optionId) {
        return answerOptionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Answer option not found"));
    }
}

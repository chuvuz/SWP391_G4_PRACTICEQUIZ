package com.quiz.g4.service;

import com.quiz.g4.entity.AnswerOption;
import org.springframework.stereotype.Service;

@Service
public interface AnswerOptionService {

    AnswerOption findById(Integer optionId);
}

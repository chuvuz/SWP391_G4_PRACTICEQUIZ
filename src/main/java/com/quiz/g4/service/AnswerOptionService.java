package com.quiz.g4.service;

import com.quiz.g4.entity.AnswerOption;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnswerOptionService {

    void save(AnswerOption answer);

    AnswerOption findById(Integer optionId);

    List<AnswerOption> findCorrectOptionsByQuestionId(Integer questionId);

    List<AnswerOption> findByQuestionId(Integer questionId);
}

package com.quiz.g4.service;

import com.quiz.g4.entity.QuestionBank;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface QuestionService {
    Page<QuestionBank> getQuestionsPaged(int page, int i);
}

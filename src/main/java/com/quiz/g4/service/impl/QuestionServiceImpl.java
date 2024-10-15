package com.quiz.g4.service.impl;

import com.quiz.g4.entity.QuestionBank;
import com.quiz.g4.repository.QuestionBankRepository;
import com.quiz.g4.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionBankRepository questionBankRepository;

    public Page<QuestionBank> getQuestionsPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return questionBankRepository.findAll(pageable);
    }
}

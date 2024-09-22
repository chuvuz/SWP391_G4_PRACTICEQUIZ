package com.quiz.g4.service.impl;

import com.quiz.g4.entity.Quiz;
import com.quiz.g4.repository.QuizRepository;
import com.quiz.g4.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository quizRepository;




}

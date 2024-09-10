package com.quiz.g4.service;

import com.quiz.g4.entity.Quiz;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuizService {

    List<Quiz> getAllQuizzes();
}

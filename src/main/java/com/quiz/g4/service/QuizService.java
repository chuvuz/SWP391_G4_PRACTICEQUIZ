package com.quiz.g4.service;

import com.quiz.g4.entity.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuizService {

    List<Quiz> getAllQuizzes();

    Page<Quiz> getAllQuizzes(int page, int size);

    Page<Quiz> searchQuizzes(String quizName, Integer subjectId, Integer expertId, int page, int size);

    List<Quiz> findQuizByAuther(Integer autherId);

    Quiz getQuizWithQuestionsAndAnswers(Integer quizId);
}

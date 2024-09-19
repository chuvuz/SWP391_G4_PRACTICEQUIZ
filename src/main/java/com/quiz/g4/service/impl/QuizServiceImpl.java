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

    @Override
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    @Override
    public List<Quiz> findQuizByAuther(Integer autherId){return quizRepository.findQuizByAuther(autherId);}

    @Override
    public Page<Quiz> getAllQuizzes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return quizRepository.findAll(pageable);
    }

    @Override
    public Page<Quiz> searchQuizzes(String quizName, Integer subjectId, Integer expertId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return quizRepository.searchQuizzes(quizName, subjectId, expertId, pageable);
    }


    @Override
    public Quiz getQuizWithQuestionsAndAnswers(Integer quizId) {
        return quizRepository.findQuizWithQuestionsAndAnswers(quizId);
    }


}

package com.quiz.g4.service.impl;

import com.quiz.g4.entity.Subject;
import com.quiz.g4.repository.QuizRepository;
import com.quiz.g4.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Override
    public List<Subject> getAllQuizzes() {
        return quizRepository.findAll();
    }

<<<<<<< Updated upstream
=======
    @Override
    public Page<Subject> getAllQuizzes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return quizRepository.findAll(pageable);
    }

    @Override
    public Page<Subject> searchQuizzes(String quizName, Integer subjectId, Integer expertId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return quizRepository.searchQuizzes(quizName, subjectId, expertId, pageable);
    }


    @Override
    public Subject getQuizWithQuestionsAndAnswers(Integer quizId) {
        return quizRepository.findQuizWithQuestionsAndAnswers(quizId);
    }


>>>>>>> Stashed changes
}

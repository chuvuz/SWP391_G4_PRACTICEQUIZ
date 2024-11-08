package com.quiz.g4.service;

import com.quiz.g4.entity.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuizService {

    List<Quiz> getAllQuizzes();

    Page<Quiz> getAllQuizzess(int page, int size);

    Page<Quiz> findActiveQuizzesByCriteria(Integer authorId, Integer subjectId, Integer lessonId, String quizName, Pageable pageable);

    List<Quiz> getQuizzesByLessonId(Integer lessonId);

    Quiz getQuizById(Integer quizId);

    Quiz getQuizWithQuestions(Integer quizId);

    Page<Quiz> findQuizByAuthor(Integer userId, Pageable pageable);

//    Quiz getQuizWithQuestionsAndAnswers(Integer quizId);
//
//    Page<Quiz> searchQuizzes(String quizName, Integer subjectId, Integer expertId, int page, int size);
//
//    Quiz getQuizWithLesson(Integer quizId);


}

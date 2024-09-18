package com.quiz.g4.service;

<<<<<<< Updated upstream
import com.quiz.g4.entity.Quiz;
=======
import com.quiz.g4.entity.Subject;
import org.springframework.data.domain.Page;
>>>>>>> Stashed changes
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuizService {

<<<<<<< Updated upstream
    List<Quiz> getAllQuizzes();
=======
    List<Subject> getAllQuizzes();

    Page<Subject> getAllQuizzes(int page, int size);

    Page<Subject> searchQuizzes(String quizName, Integer subjectId, Integer expertId, int page, int size);


    Subject getQuizWithQuestionsAndAnswers(Integer quizId);
>>>>>>> Stashed changes
}

package com.quiz.g4.service;

import com.quiz.g4.entity.QuestionBank;
import com.quiz.g4.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionBankService {

    /*
        QuestionBank createQuestion(QuestionRequest questionRequest);
    */
    QuestionBank findById(Integer id);

    boolean existsByQuestionContent(String questionContent);

    void save(QuestionBank question);

    Page<QuestionBank> allQuestions(Pageable pages);

    Page<QuestionBank> searchQuestion(String questionContent, String questionType, Integer subject, Integer lesson, Pageable pages);

    boolean checkCorrectAnswer(Integer questionId, Integer selectedOptionId);

    List<QuestionBank> getQuestionsBySubjectId(Subject subjectId);

    List<QuestionBank> getAllQuestions();

    List<QuestionBank> getQuestionsByIds(List<Integer> selectedQuestions);
}

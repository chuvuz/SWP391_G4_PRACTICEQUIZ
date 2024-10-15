package com.quiz.g4.service.impl;

import com.quiz.g4.entity.AnswerOption;
import com.quiz.g4.entity.QuestionBank;
import com.quiz.g4.repository.AnswerOptionRepository;
import com.quiz.g4.repository.QuestionBankRepository;
import com.quiz.g4.service.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionBankServiceImpl implements QuestionBankService {

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @Autowired
    private QuestionBankRepository questionBankRepository;

    @Override
    public boolean checkCorrectAnswer(Integer questionId, Integer selectedOptionId) {
        return answerOptionRepository.findById(selectedOptionId)
                .map(option -> option.getQuestionBank().getQuestionId().equals(questionId) && option.getIsCorrect())
                .orElse(false);
    }

    @Override
    public List<QuestionBank> allQuestions(){
        return questionBankRepository.findAll();
    }

}

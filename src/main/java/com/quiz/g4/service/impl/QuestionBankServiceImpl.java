package com.quiz.g4.service.impl;

import com.quiz.g4.entity.AnswerOption;
import com.quiz.g4.entity.QuestionBank;
import com.quiz.g4.repository.AnswerOptionRepository;
import com.quiz.g4.repository.QuestionBankRepository;
import com.quiz.g4.service.QuestionBankService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
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
    public void save(QuestionBank question) {
        questionBankRepository.save(question);
    }

    @Override
    public Page<QuestionBank> allQuestions(Pageable pages){
        return questionBankRepository.findAll(pages);
    }

    @Override
    public Page<QuestionBank> searchQuestion(Pageable pages, String questionContent, String questionType){
        return questionBankRepository.searchQuestion(questionContent, questionType, pages);
    }

    /*@Transactional
    public QuestionBank createQuestion(QuestionRequest questionRequest) {
        // Tạo đối tượng QuestionBank
        QuestionBank questionBank = QuestionBank.builder()
                .questionContent(questionRequest.getContent())
                .questionType(questionRequest.getType())
                .build();

        // Lưu câu hỏi vào cơ sở dữ liệu
        questionBank = questionBankRepository.save(questionBank);

        // Tạo và liên kết các AnswerOption
        Set<AnswerOption> answerOptions = new HashSet<>();
        for (QuestionRequest.AnswerRequest answer : questionRequest.getAnswers()) {
            AnswerOption answerOption = AnswerOption.builder()
                    .questionBank(questionBank)
                    .content(answer.getContent())
                    .isCorrect(answer.isCorrect())
                    .build();
            answerOptions.add(answerOptionRepository.save(answerOption));
        }

        // Liên kết các câu trả lời với câu hỏi
        questionBank.setAnswerOptions(answerOptions);

        return questionBank;
    }*/

}

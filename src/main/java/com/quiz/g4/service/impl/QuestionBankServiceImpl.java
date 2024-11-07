package com.quiz.g4.service.impl;

import com.quiz.g4.entity.Lesson;
import com.quiz.g4.entity.QuestionBank;
import com.quiz.g4.entity.Subject;
import com.quiz.g4.repository.AnswerOptionRepository;
import com.quiz.g4.repository.LessonRepository;
import com.quiz.g4.repository.QuestionBankRepository;
import com.quiz.g4.repository.SubjectRepository;
import com.quiz.g4.service.QuestionBankService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionBankServiceImpl implements QuestionBankService {

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @Autowired
    private QuestionBankRepository questionBankRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private LessonRepository lessonRepository;

    public QuestionBank findById(Integer id) {
        return questionBankRepository.findByQuestionId(id);
    }

    @Override
    public boolean checkCorrectAnswer(Integer questionId, Integer selectedOptionId) {
        return answerOptionRepository.findById(selectedOptionId)
                .map(option -> option.getQuestionBank().getQuestionId().equals(questionId) && option.getIsCorrect())
                .orElse(false);
    }

    @Override
    public List<QuestionBank> getQuestionsBySubjectId(Subject subjectId) {
        return questionBankRepository.findBySubject_Id(subjectId);
    }

    @Override
    public List<QuestionBank> getAllQuestions() {
        return questionBankRepository.findAll();
    }

    @Override
    public List<QuestionBank> getQuestionsByIds(List<Integer> selectedQuestions) {
        return questionBankRepository.findAllById(selectedQuestions);
    }

    @Override
    public boolean existsByQuestionContent(String questionContent) {
        return questionBankRepository.existsByQuestionContent(questionContent);
    }

    @Override
    public void save(QuestionBank question) {
        questionBankRepository.save(question);
    }

    @Override
    public Page<QuestionBank> allQuestions(Pageable pages) {
        return questionBankRepository.findAll(pages);
    }

    @Override
    public Page<QuestionBank> searchQuestion(String questionContent, String questionType, Integer subjectId, Integer lessonId, Pageable pages) {
        return questionBankRepository.searchQuestion(questionContent, questionType, subjectId, lessonId, pages);
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

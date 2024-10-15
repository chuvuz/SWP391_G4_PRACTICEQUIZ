package com.quiz.g4.controller.questionBank;

import com.quiz.g4.entity.AnswerOption;
import com.quiz.g4.service.AnswerOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestQuestionController {

    @Autowired
    private AnswerOptionService answerOptionService;

    // API để lấy danh sách câu trả lời cho một câu hỏi
    @GetMapping("/answers/{questionId}")
    public ResponseEntity<List<AnswerOption>> getAnswersForQuestion(@PathVariable("questionId") Integer questionId) {
        List<AnswerOption> answers = answerOptionService.findByQuestionId(questionId);
        return ResponseEntity.ok(answers);
    }
}

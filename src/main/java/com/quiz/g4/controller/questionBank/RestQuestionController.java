/*
package com.quiz.g4.controller.questionBank;

import com.quiz.g4.dto.QuestionRequest;
import com.quiz.g4.entity.AnswerOption;
import com.quiz.g4.entity.QuestionBank;
import com.quiz.g4.service.AnswerOptionService;
import com.quiz.g4.service.QuestionBankService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class RestQuestionController {

    @Autowired
    private AnswerOptionService answerOptionService;

    // API để lấy danh sách câu trả lời cho một câu hỏi
    @GetMapping("/answers/{questionId}")
    public ResponseEntity<List<AnswerOption>> getAnswersForQuestion(@PathVariable("questionId") Integer questionId) {
        List<AnswerOption> answers = answerOptionService.findByQuestionId(questionId);
        return ResponseEntity.ok(answers);
    }

    @Autowired
    private final QuestionBankService questionBankService;

    @PostMapping("/create")
    public QuestionBank createQuestion(@RequestBody QuestionRequest questionRequest) {
        return questionBankService.createQuestion(questionRequest);
    }
}
*/

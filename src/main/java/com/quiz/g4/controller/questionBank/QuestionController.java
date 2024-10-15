package com.quiz.g4.controller.questionBank;

import com.quiz.g4.entity.AnswerOption;
import com.quiz.g4.entity.QuestionBank;
import com.quiz.g4.service.AnswerOptionService;
import com.quiz.g4.service.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionBankService questionBankService;


    @GetMapping("/questions")
    public String questions (
            @RequestParam(defaultValue = "0") int page,  // Default to first page
            @RequestParam(defaultValue = "15") int size,  // Default page size of 5
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<QuestionBank> questionPage = questionBankService.allQuestions(pageable);

        model.addAttribute("questionPage", questionPage);
        return "/question_bank";
    }

    @GetMapping("/search_questions")
    public String search_questions (
            @RequestParam(value = "questionContent", required = false) String questionContent,
            @RequestParam(value = "questionType", required = false) String questionType,
            @RequestParam(defaultValue = "0") int page,  // Default to first page
            @RequestParam(defaultValue = "15") int size,  // Default page size of 5
            Model model) {
        if(questionContent.trim().isEmpty() && questionType.trim().isEmpty()){
            Pageable pageable = PageRequest.of(page, size);
            Page<QuestionBank> questionPage = questionBankService.allQuestions(pageable);
            model.addAttribute("questionPage", questionPage);
            model.addAttribute("questionContent", questionContent);
            model.addAttribute("questionType", questionType);
        }else {
            Pageable pageable = PageRequest.of(page, size);
            Page<QuestionBank> questionPage = questionBankService.searchQuestion(pageable, questionContent, questionType);
            model.addAttribute("questionPage", questionPage);
            model.addAttribute("questionContent", questionContent);
            model.addAttribute("questionType", questionType);
        }


        return "/question_bank";
    }

}




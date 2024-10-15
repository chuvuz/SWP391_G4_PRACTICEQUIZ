package com.quiz.g4.controller.questionBank;

import com.quiz.g4.entity.AnswerOption;
import com.quiz.g4.entity.QuestionBank;
import com.quiz.g4.service.AnswerOptionService;
import com.quiz.g4.service.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionBankService questionBankService;


    @GetMapping("/questions")
    public String questions (Model model){
            List<QuestionBank> question = questionBankService.allQuestions();
            model.addAttribute("question", question);
        return "/question_bank";
    }

}




package com.quiz.g4.controller.questionBank;

import com.quiz.g4.dto.AnswerOptionForm;
import com.quiz.g4.dto.QuestionForm;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionBankService questionBankService;

    @Autowired
    private AnswerOptionService answerOptionService;

    @GetMapping("/questionlist")
    public String questions (
            @RequestParam(defaultValue = "0") int page,  // Default to first page
            @RequestParam(defaultValue = "15") int size,  // Default page size of 5
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<QuestionBank> questionPage = questionBankService.allQuestions(pageable);

        model.addAttribute("questionPage", questionPage);
        return "/QuestionBank/question_bank";
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


        return "/QuestionBank/question_bank";
    }

    @GetMapping("/add_Question")
    public String addQuestion(){
        return "/QuestionBank/addQuestion";
    }

    @PostMapping("/createQuestions")
    public String createQuestion (Model model, QuestionForm questionForm) {

        // Kiểm tra xem câu hỏi đã tồn tại chưa
        if (questionBankService.existsByQuestionContent(questionForm.getQuestionContent())) {
            model.addAttribute("error", "Question already exists!"); // Thêm thông báo lỗi
            return "/QuestionBank/addQuestion"; // Trở về trang thêm câu hỏi
        }

        // Tạo QuestionBank
        QuestionBank questionBank = new QuestionBank();
        questionBank.setQuestionContent(questionForm.getQuestionContent());
        questionBank.setQuestionType(questionForm.getQuestionType());
        questionBankService.save(questionBank);

        /*// Tạo AnswerOption từ form
        for (AnswerOptionForm answerOptionForm : questionForm.getAnswerOptions()) {
            AnswerOption answerOption = new AnswerOption();
            answerOption.setQuestionBank(questionBank);
            answerOption.setContent(answerOptionForm.getContent());
            answerOption.setIsCorrect(answerOptionForm.getCorrect());
            answerOptionService.save(answerOption);
        }*/

        return "redirect:/questionlist";  // Chuyển hướng sau khi lưu xong
    }

    @GetMapping("/updateQuestion/{id}")
    public String viewQuestion(@PathVariable Integer id, Model model) {
        QuestionBank question = questionBankService.findById(id);
        model.addAttribute("question", question);
        return "/QuestionBank/UpdateQuestion"; // Trang hiển thị thông tin câu hỏi
    }

}




package com.quiz.g4.controller.questionBank;

import com.quiz.g4.entity.AnswerOption;
import com.quiz.g4.entity.QuestionBank;
import com.quiz.g4.service.AnswerOptionService;
import com.quiz.g4.service.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    /*@GetMapping("/search_questions")
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
    }*/

    @GetMapping("/add_Question")
    public String addQuestion(){
        return "/QuestionBank/addQuestion";
    }

    @PostMapping("/createQuestions")
    public String createQuestion (Model model,
                                  @RequestParam String questionContent,
                                  @RequestParam String questionType,
                                  @RequestParam List<String> answerContent,
                                  @RequestParam List<Boolean> answerIsCorrect
    ) {

        for (int i = 0; i < answerIsCorrect.size(); i++){
            System.out.println(answerIsCorrect.get(i).toString());
        }

        // Kiểm tra xem câu hỏi đã tồn tại chưa
        Set<String> setContent = new HashSet<>();
        for (int i = 0; i < answerContent.size(); i++){
            setContent.add(answerContent.get(i));
        }
        boolean correct = false;
        for (int i = 0; i < answerIsCorrect.size(); i++){
            if(answerIsCorrect.get(i) == true){
                correct = true;
                break;
            }
        }
        if(setContent.size() < answerContent.size() || !correct){
            model.addAttribute("error", "đáp án bị trùng hoặc không có đáp án đúng vui lòng thử lại!");
            return "/QuestionBank/addQuestion";
        } else if (questionBankService.existsByQuestionContent(questionContent)){
            model.addAttribute("error", "câu hỏi đã tồn tại!");
            return "/QuestionBank/addQuestion";
        }


            // Tạo QuestionBank
        QuestionBank questionBank = new QuestionBank();
        questionBank.setQuestionContent(questionContent);
        questionBank.setQuestionType(questionType);
        questionBankService.save(questionBank);


        // Tạo AnswerOption từ form
        Set<AnswerOption> answerOptions = new HashSet<>();
        for (int i = 0; i < answerContent.size(); i++) {
            AnswerOption option = new AnswerOption();
            option.setQuestionBank(questionBank);
            option.setContent(answerContent.get(i));
            if(answerIsCorrect.get(i) != null){
                option.setIsCorrect(true);
            }else{
                option.setIsCorrect(true);
            }// Xác định đáp án đúng
            answerOptionService.save(option);
        }

        return "redirect:/questionlist";  // Chuyển hướng sau khi lưu xong
    }

    @GetMapping("/question/{id}")
    public String viewQuestion(@PathVariable Integer id, Model model) {
        QuestionBank question = questionBankService.findById(id);
        model.addAttribute("question", question);
        return "/QuestionBank/UpdateQuestion"; // Trang hiển thị thông tin câu hỏi
    }

    @PostMapping("/question/update")
    public String updateQuestion(Model model,
                                 @RequestParam("id") Integer id,
                                 @RequestParam("questionContent")  String questionContent,
                                 @RequestParam("questionType") String questionType
    ) {
        // Tìm câu hỏi hiện tại
        QuestionBank questionBank = questionBankService.findById(id);

        if (questionBank == null) {
            return "redirect:/questionlist"; // Hoặc hiển thị thông báo không tìm thấy
        }

        // Cập nhật thông tin câu hỏi
        questionBank.setQuestionContent(questionContent);
        questionBank.setQuestionType(questionType);
        questionBankService.save(questionBank);

        /*//Cập nhật các câu trả lời
        for (AnswerOptionForm answerOptionForm : questionForm.getAnswerOptions()) {
            // Kiểm tra xem câu trả lời đã tồn tại chưa, nếu chưa thì thêm mới
            AnswerOption answerOption = answerOptionService.findByContentAndQuestion(questionBank, answerOptionForm.getContent());
            if (answerOption == null) {
                answerOption = new AnswerOption();
                answerOption.setQuestionBank(questionBank);
            }
            answerOption.setContent(answerOptionForm.getContent());
            answerOption.setIsCorrect(answerOptionForm.getCorrect());
            answerOptionService.save(answerOption);
        }*/

        return "redirect:/questionlist";  // Chuyển hướng sau khi cập nhật xong
    }



}




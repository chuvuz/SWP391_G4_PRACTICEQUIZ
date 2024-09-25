package com.quiz.g4.controller;

import com.quiz.g4.entity.Quiz;
import com.quiz.g4.entity.Subject;
import com.quiz.g4.entity.User;
import com.quiz.g4.service.QuizService;
import com.quiz.g4.service.SubjectService;
import com.quiz.g4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@Controller
public class ExpertController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/expert")
    public String getExpertlist(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "8") int size){
        page = Math.max(page, 0);
        // Lấy danh sách các user có role_id = 3 (ROLE_EXPERT)
        Page<User> expertPage = userService.getAllExpert(page, size);
        // Đưa danh sách này vào model
        model.addAttribute("expertPage", expertPage);
        return "experts";
    }

    @GetMapping("/expert_detail/{expertId}")
    public String getExpertDetails(Model model,
                                   @PathVariable("expertId") Integer userId){
        // Lấy danh sách các subject và expert để hiển thị
        User expert = userService.findUserByUserId(userId);
        Subject subject = expert.getSubject();
        model.addAttribute("subject", subject);
        model.addAttribute("expert", expert);

        // Lấy danh sách quiz
        List<Quiz> quizzess = quizService.findQuizByAuther(expert.getUserId());
        model.addAttribute("quizzess", quizzess);
        return "expert_details";
    }


}

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

    @GetMapping("/expert_detail")
    public String getExpertDetails(Model model,
                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "size", defaultValue = "10") int size){
        // Lấy danh sách các subject và expert để hiển thị
        List<Subject> subjects = subjectService.getAllSubjects();
        List<User> experts = userService.findByRoleId(3); // Expert có role_id = 3
        model.addAttribute("subjects", subjects);
        model.addAttribute("experts", experts);

        // Lấy danh sách quiz
        Page<Quiz> quizPage = quizService.getAllQuizzes(page, size);
        model.addAttribute("quizPage", quizPage);
        return "expert_details";
    }


}

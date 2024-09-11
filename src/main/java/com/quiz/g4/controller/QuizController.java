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
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UserService userService;

    @GetMapping("/quiz-list")
    public String quizList(Model model,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "10") int size){

        // Tạo một danh sách subject từ service
        List<Subject> subjects = subjectService.getAllSubjects();
        model.addAttribute("subjects", subjects);

        // Lấy danh sách quiz
        Page<Quiz> quizPage = quizService.getAllQuizzes(page, size);
        model.addAttribute("quizPage", quizPage);

        // Lấy danh sách các user có role_id = 3 (ROLE_EXPERT)
        List<User> experts = userService.findByRoleId(3);
        model.addAttribute("experts", experts);


        return "quiz-list";
    }
}

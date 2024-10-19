package com.quiz.g4.controller;

import com.quiz.g4.entity.Subject;
import com.quiz.g4.entity.User;
import com.quiz.g4.service.SubjectService;
import com.quiz.g4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
public class SubjectController {

    @Autowired
    private UserService userService;

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/subject-list")
    public String quizList(Model model,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "9") int size) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            model.addAttribute("user", user);  // Thêm thông tin người dùng vào model
        }

        // Lấy danh sách Subject
        Page<Subject> subjectsPage = subjectService.getAllSubject(page, size);
        model.addAttribute("subjectsPage", subjectsPage);

        return "quiz/subject-list"; // Trả về view quiz-list
    }
}

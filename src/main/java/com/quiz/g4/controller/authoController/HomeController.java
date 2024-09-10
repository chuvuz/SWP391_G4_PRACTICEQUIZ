package com.quiz.g4.controller.authoController;

import com.quiz.g4.entity.Blog;
import com.quiz.g4.entity.Quiz;
import com.quiz.g4.entity.Subject;
import com.quiz.g4.entity.User;
import com.quiz.g4.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Objects;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private BlogService blogService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            model.addAttribute("user", user);
        }

        // Tạo một danh sách subject từ service
        List<Subject> subjects = subjectService.getAllSubjects();

        // Đưa danh sách subject vào model
        model.addAttribute("subjects", subjects);


        // Lấy danh sách quiz
        List<Quiz> quizzes = quizService.getAllQuizzes();
        model.addAttribute("quizzes", quizzes);

        // Lấy danh sách các user có role_id = 3 (ROLE_EXPERT)
        List<User> experts = userService.findByRoleId(3);
        // Đưa danh sách này vào model
        model.addAttribute("experts", experts);

        List<Blog> blogs = blogService.findAllBlogs();
        model.addAttribute("blogs", blogs);
        return "home";
    }
}

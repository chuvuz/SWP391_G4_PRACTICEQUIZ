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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
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
    public String home(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.nonNull(authentication) && authentication.isAuthenticated()
                && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            model.addAttribute("user", user);
        }

        // Tạo danh sách subjects từ service
        List<Subject> subjects = subjectService.getAllSubjects();
        model.addAttribute("subjects", subjects);

        // Lấy danh sách blogs từ service
        List<Blog> blogs = blogService.getAllBlogs();
        model.addAttribute("blogs", blogs);

        // Lấy danh sách Expert
        List<User> experts = userService.findByRoleId(3);
        model.addAttribute("experts", experts);

        return "home";
    }


}

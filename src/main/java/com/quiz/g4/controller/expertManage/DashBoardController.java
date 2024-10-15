package com.quiz.g4.controller.expertManage;

import com.quiz.g4.entity.Quiz;
import com.quiz.g4.entity.User;
import com.quiz.g4.service.QuizService;
import com.quiz.g4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Objects;

@Controller
public class DashBoardController {
    @Autowired
    private UserService userService;
    @Autowired
    private QuizService quizService;
    @GetMapping("/expert/expert_dashboard")
    public String getDoashboard(){

        return "expert_dashboard";
    }

    @GetMapping("/expert/expert_quizz")
    public String getExpertQuizz(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (Objects.nonNull(authentication) && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            user = userService.findByEmail(email);
            model.addAttribute("user", user);
            List<Quiz> quizzes = quizService.findQuizByAuther(user.getUserId());
            model.addAttribute("quizzes", quizzes);
            return "/quiz/expert_quizz";
        }

        List<Quiz> quizzes = quizService.findQuizByAuther(user.getUserId());
        model.addAttribute("quizzes", quizzes);
        return "/quiz/expert_quizz";
    }

}

package com.quiz.g4.controller.adminManage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.quiz.g4.entity.Quiz;
import com.quiz.g4.entity.Subject;
import com.quiz.g4.entity.User;
import com.quiz.g4.service.QuizService;
import com.quiz.g4.service.SubjectService;
import com.quiz.g4.service.UserService;
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String viewUserList(@RequestParam(required = false) String role, Model model) {
        List<User> users;
        if (role != null) {
            users = userService.findByRole(role);
        } else {
            users = userService.findAllExceptGuest();
        }
        model.addAttribute("users", users);
        return "admin/userList";
    }

    @PostMapping("/users/status")
    public String updateUserStatus(@RequestParam Long userId, @RequestParam boolean active) {
        userService.updateUserStatus(userId, active);
        return "redirect:/admin/users";
    }
}


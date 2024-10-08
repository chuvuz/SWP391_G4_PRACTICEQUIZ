package com.quiz.g4.controller.adminManage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.quiz.g4.entity.Quiz;
import com.quiz.g4.entity.Subject;
import com.quiz.g4.entity.User;
import com.quiz.g4.service.QuizService;
import com.quiz.g4.service.SubjectService;
import com.quiz.g4.service.UserService;

import java.util.List;

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
        return "admin/view-user-list";
    }

    @PostMapping("/users/status")
    public String updateUserStatus(@RequestParam Integer userId, @RequestParam boolean active) {
        userService.updateUserStatus(userId, active);
        return "redirect:/admin/users";
    }
    @GetMapping("/users/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/createUser";
    }

    @PostMapping("/users/create")
    public String createUser(@ModelAttribute User user) {
        userService.createUser(user);
        return "redirect:/admin/users";
    }
}


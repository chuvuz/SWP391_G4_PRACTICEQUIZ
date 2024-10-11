package com.quiz.g4.controller.adminManage;

import com.quiz.g4.dto.UserDTO;
import com.quiz.g4.entity.Role;
import com.quiz.g4.service.EmailService;
import com.quiz.g4.utils.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping("/users")
    public String viewUserList(@RequestParam(required = false) String role, Model model) {
        List<User> users;
        if (role != null) {
            users = userService.findByRole(role);
        } else {
            users = userService.findAllExceptAdminAndGuest();
        }
        model.addAttribute("users", users);
        return "admin/view-user-list";
    }

    @PostMapping("/users/status")
    public String updateUserStatus(@RequestParam Integer userId, @RequestParam boolean active) {
        userService.updateUserStatus(userId, active);
        return "redirect:/";
    }
    @GetMapping("/users/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/create-user";
    }

    @PostMapping("/users/create")
    public String createUser(@ModelAttribute UserDTO user) {
        System.out.println(user.toString());
        User user1=null;
        user1.setFullName(user.getFullName());
        user1.setEmail(user.getEmail());
        Role role = null;
        role.setRoleId(2);
        role.setRoleName(user.getRoleName());
        user1.setRole(role);
        String rawPassword = PasswordGenerator.generatePassword();
        String encryptedPassword = passwordEncoder.encode(rawPassword);
        user1.setPassword(encryptedPassword);
        user1.setIsActive(true); // Set default status as active

        userService.createUser(user1);

        // Send email to user with the raw password
        emailService.sendEmail(user.getEmail(), "Your New Account", "Your password is: " + rawPassword);

        return "redirect:/";
    }
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("totalUsers", userService.countTotalUsers());
        model.addAttribute("activeUsers", userService.countActiveUsers());
        model.addAttribute("inactiveUsers", userService.countInactiveUsers());
        return "admin/admin-dashboard";
    }
}


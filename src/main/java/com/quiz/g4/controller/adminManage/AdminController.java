package com.quiz.g4.controller.adminManage;

import com.quiz.g4.dto.UserDTO;
import com.quiz.g4.entity.Role;
import com.quiz.g4.service.*;
import com.quiz.g4.utils.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.quiz.g4.entity.Quiz;
import com.quiz.g4.entity.Subject;
import com.quiz.g4.entity.User;

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
    @Autowired
    private RoleService roleService;

    //    @GetMapping("/users")
//    public String viewUserList(@RequestParam(required = false) String role, Model model) {
//        List<User> users;
//        if (role != null) {
//            users = userService.findByRole(role);
//        } else {
//            users = userService.findAllExceptAdminAndGuest();
//        }
//        List<Role> roles = roleService.findRolesForUserCreation();
//        model.addAttribute("roles", roles);
//        model.addAttribute("users", users);
//        return "admin/view-user-list";
//    }
    @GetMapping("/users")
    public String viewUserList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String role,
            Model model) {
        Page<User> userPage;
        if (role != null && !role.isEmpty()) {
            userPage = userService.findByRole(role, PageRequest.of(page, size));
        } else {
            userPage = userService.findAllExceptAdminAndGuest(PageRequest.of(page, size));
        }
        List<Role> roles = roleService.findRolesForUserCreation();
        model.addAttribute("roles", roles);
        model.addAttribute("userPage", userPage);
        return "admin/view-user-list";
    }


    @PostMapping("/users/update-status")
    public String updateUserStatus(@RequestParam Integer userId) {
        User user = userService.findUserByUserId(userId);
        userService.changeUserStatus(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/create")
    public String showCreateUserForm(Model model) {
        List<Role> roles = roleService.findRolesForUserCreation();
        model.addAttribute("roles", roles); // Add roles to the model
        model.addAttribute("user", new User());
        return "admin/create-user";
    }

    @PostMapping("/users/create")
    public String createUser(@ModelAttribute User user) {
        System.out.println("Creating user with fullName: " + user.getFullName());
        System.out.println("Creating user with email: " + user.getEmail());
        System.out.println("Creating user with role: " + user.getRole().getRoleName());
        String rawPassword = PasswordGenerator.generatePassword();
        String encryptedPassword = passwordEncoder.encode(rawPassword);

        user.setPassword(encryptedPassword);
        user.setIsActive(true); // Set default status as active
        userService.createUser(user);
        // Send email to user with the raw password
        emailService.sendEmail(user.getEmail(), "Your New Account", "Your password is: " + rawPassword);

        return "redirect:/admin/users";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("totalUsers", userService.countTotalUsers());
        model.addAttribute("activeUsers", userService.countActiveUsers());
        model.addAttribute("inactiveUsers", userService.countInactiveUsers());
        return "admin/admin-dashboard";
    }
}


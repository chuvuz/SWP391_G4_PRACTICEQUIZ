package com.quiz.g4.controller;
import com.quiz.g4.entity.User;
import com.quiz.g4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
@Controller

public class UserProfileController {
    @Autowired
    private UserService userService;


    @GetMapping("/profile")
    //Principal tra ve current user
    public String getUserProfile(Principal principal, Model model) {
        try {
            String username = principal.getName();

            User user = (User) userService.loadUserByUsername(username);
            model.addAttribute("user", user);
            return "profile";
        }catch (Exception e){
            return e.getMessage();
        }
    }
    @GetMapping("/edit")
    public String editUserProfile(Model model, Authentication authentication) {
        String email = authentication.getName();
        try {
            User user = userService.findByEmail(email);
            model.addAttribute("user", user);
            return "editProfile";
        } catch (Exception e) {
            return "error";
        }
    }

    @PostMapping("/edit")
    public String updateUserProfile(@ModelAttribute User updatedUser, Authentication authentication) {
        String email = authentication.getName();
        try {
            userService.updateUser(email, updatedUser);
            return "redirect:/profile";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/change-password")
    public String changePasswordForm(Model model) {
        model.addAttribute("passwordForm", new PasswordForm());
        return "changePassword";
    }

    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute PasswordForm passwordForm, Authentication authentication) {
        String email = authentication.getName();
        if (!passwordForm.getNewPassword().equals(passwordForm.getConfirmPassword())) {
            return "error";
        }
        userService.changePassword(email, passwordForm.getNewPassword());
        return "redirect:/profile";
    }

}

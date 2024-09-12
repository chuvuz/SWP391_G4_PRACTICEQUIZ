package com.quiz.g4.controller;
import com.quiz.g4.entity.User;
import com.quiz.g4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;
@Controller
@RequestMapping("/profile")
public class UserProfileController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyRole('ADMIN', 'EXPERT', 'CUSTOMER', 'MARKETING')")
    @GetMapping
    public String getUserProfile(Principal principal, Model model) {
        String username = principal.getName();
        //chua chay
        //User user = userService.getUserProfile(username);
        //model.addAttribute("user", user);
        return "profile";
    }

}

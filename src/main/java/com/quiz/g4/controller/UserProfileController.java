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

}

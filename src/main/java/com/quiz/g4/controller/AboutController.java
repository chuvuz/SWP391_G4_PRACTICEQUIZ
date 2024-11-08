package com.quiz.g4.controller;

import com.quiz.g4.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Objects;

import com.quiz.g4.service.*;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class AboutController {

    @Autowired
    private UserService userService;

    @GetMapping("/about")
    public String aboutUs(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            model.addAttribute("user", user);  // Thêm thông tin người dùng vào model
        }

        // Lấy danh sách các user có role_id = 3 (ROLE_EXPERT)
        List<User> experts = userService.findByRoleId(3);
        // Đưa danh sách này vào model
        model.addAttribute("experts", experts);
        return "about";
    }
}

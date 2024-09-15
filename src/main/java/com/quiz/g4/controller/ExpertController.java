package com.quiz.g4.controller;

import com.quiz.g4.entity.User;
import com.quiz.g4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ExpertController {
    @Autowired
    private UserService userService;

    @GetMapping("/expert")
    public String getAllExpert(Model model){
        // Lấy danh sách các user có role_id = 3 (ROLE_EXPERT)
        List<User> experts = userService.findByRoleId(3);
        // Đưa danh sách này vào model
        model.addAttribute("experts", experts);
        return "expert";
    }

}

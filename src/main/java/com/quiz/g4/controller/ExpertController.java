package com.quiz.g4.controller;

import com.quiz.g4.entity.User;
import com.quiz.g4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ExpertController {
    @Autowired
    private UserService userService;

    @GetMapping("/expert")
    public String getAllExpert(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "8") int size){
        page = Math.max(page, 0);
        // Lấy danh sách các user có role_id = 3 (ROLE_EXPERT)
        Page<User> expertPage = userService.getAllExpert(page, size);
        // Đưa danh sách này vào model
        model.addAttribute("expertPage", expertPage);
        return "experts";
    }

}

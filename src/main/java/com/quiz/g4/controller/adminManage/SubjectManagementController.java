package com.quiz.g4.controller.adminManage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class SubjectManagementController {
    @GetMapping("/manage-subject")
    public String manageSubject(){
        return "admin/manage-subject";
    }
}
package com.quiz.g4.controller.adminManage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.awt.*;

@Controller
public class ExpertManageController {

    @GetMapping("/manage-expert")
    public String manageExpert(){

        return "admin/manage-expert";
    }

}

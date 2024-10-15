package com.quiz.g4.controller.adminManage;

import com.quiz.g4.entity.Subject;
import com.quiz.g4.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import com.quiz.g4.service.SubjectService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Optional;

@Controller
public class SubjectManagementController {

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private SubjectRepository subjectRepository;
    @GetMapping("/manage-subject")
    public String manageSubject(Model model) {
        model.addAttribute("subjects", subjectService.getAllSubjects());
        return "admin/manage-subject";
    }


    @PostMapping("/create-subject")
    public String createSubject(@RequestParam String subjectName,
                                @RequestParam boolean isActive) {
        subjectService.createSubject(subjectName, isActive);
        return "redirect:/manage-subject";
    }

    @GetMapping("/edit-subject/{subjectId}")
    public String editSubjectForm(Model model, @PathVariable Integer subjectId) {
        Subject subject = subjectService.findById(subjectId);
        model.addAttribute("subject",subject);
        return "admin/edit-subject";
    }

    @PostMapping("/edit-subject")
    public String updateSubject(
            @RequestParam("subjectId") int id,
            @RequestParam("subjectName") String subjectName,
            @RequestParam("isActive") boolean isActive
    ) {
        // Gọi đến phương thức updateSubject với các tham số mới
        subjectService.updateSubject(id, subjectName, isActive);

        // Chuyển hướng đến trang quản lý subject sau khi cập nhật thành công
        return "redirect:/manage-subject";
    }



}
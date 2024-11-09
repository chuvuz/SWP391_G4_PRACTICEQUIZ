package com.quiz.g4.controller;

import com.quiz.g4.entity.Quiz;
import com.quiz.g4.entity.Subject;
import com.quiz.g4.entity.User;
import com.quiz.g4.service.LessonService;
import com.quiz.g4.service.QuizService;
import com.quiz.g4.service.SubjectService;
import com.quiz.g4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@Controller
public class ExpertController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private LessonService lessonService;

    @GetMapping("/experts")
    public String getExpertlist(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "8") int size) {
        // Lấy danh sách các subject để hiển thị
        Pageable pageable = PageRequest.of(page,size);
        Page<User> expertPage = userService.searchExpert("", pageable);
        model.addAttribute("expertPage", expertPage);
        return "experts";
    }

    @GetMapping("/search_expert")
    public String searchExpert(@RequestParam(value = "expertName", required = false) String expertName,
                               @RequestParam(value = "subjectId", required = false) Integer subjectId,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "8") int size,
                               Model model
    ) {
        // Lấy danh sách các subject để hiển thị
        Pageable pageable = PageRequest.of(page,size);
        Page<User> expertPage = userService.searchExpert(expertName, pageable);
        model.addAttribute("expertPage", expertPage);
        return "experts";
    }


    @GetMapping("/expert_detail/{expertId}")
    public String getExpertDetails(Model model,
                                   @PathVariable("expertId") Integer userId,
                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "size", defaultValue = "12") int size) {
        // Retrieve the expert and subjects list for display
        User expert = userService.findUserByUserId(userId);
        List<Subject> subjects = subjectService.getAllSubjects();
        model.addAttribute("subject", subjects);
        model.addAttribute("expert", expert);

        // Fetch only active quizzes
        Pageable pageable = PageRequest.of(page, size);
        Page<Quiz> quizzess = quizService.findActiveQuizzesByCriteria(userId, null, null, null, pageable);
        model.addAttribute("quizzess", quizzess);

        return "expert_details";
    }

    @GetMapping("/expert_detail/searchQuizzes")
    public String getExpertDetailsQuizzes(Model model,
                                          @RequestParam("expertId") Integer userId,
                                          @RequestParam(value = "subjectId", required = false) Integer subjectId,
                                          @RequestParam(value = "lessonId", required = false) Integer lessonId,
                                          @RequestParam(value = "quizName", required = false) String quizName,
                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "size", defaultValue = "12") int size) {
        User expert = userService.findUserByUserId(userId);
        model.addAttribute("expert", expert);

        Pageable pageable = PageRequest.of(page, size);
        Page<Quiz> quizzess = quizService.findActiveQuizzesByCriteria(expert.getUserId(), subjectId, lessonId, quizName, pageable);
        model.addAttribute("quizzess", quizzess);

        // Truyền lại các giá trị vào model để Thymeleaf có thể sử dụng
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("lessonId", lessonId);
        model.addAttribute("quizName", quizName);
        model.addAttribute("subject", subjectService.getAllSubjects()); // Hoặc cách lấy subjects từ service
        model.addAttribute("lessons", lessonService.getLessonsBySubjectId(subjectId)); // Dựa trên subjectId nếu có

        return "expert_details";
    }

}

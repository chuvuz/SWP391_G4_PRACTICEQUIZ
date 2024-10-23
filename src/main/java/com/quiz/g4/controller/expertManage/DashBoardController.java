package com.quiz.g4.controller.expertManage;

import com.quiz.g4.entity.*;
import com.quiz.g4.service.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Controller
public class DashBoardController {
    @Autowired
    private UserService userService;
    @Autowired
    private QuizService quizService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private QuestionBankService questionBankService;
    @Autowired
    private SubjectService subjectService;

    @GetMapping("/expert/expert_dashboard")
    public String getDoashboard(){

        return "expert_dashboard";
    }

    @GetMapping("/expert/expert_quizz")
    public String getExpertQuizz(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (Objects.nonNull(authentication) && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            user = userService.findByEmail(email);
            model.addAttribute("user", user);
            List<Quiz> quizzes = quizService.findQuizByAuther(user.getUserId());
            model.addAttribute("quizzes", quizzes);

            return "/quiz/expert_quizz";
        }

        List<Quiz> quizzes = quizService.findQuizByAuther(user.getUserId());
        model.addAttribute("quizzes", quizzes);
        return "/quiz/expert_quizz";
    }


    @GetMapping("/expert/expert_manage_question")
    public String getAllQuestionsPaged(@RequestParam(defaultValue = "0") int page,  // Default to first page
                                       @RequestParam(defaultValue = "15") int size,  // Default page size of 5
                                       Model model){
        Pageable pageable = PageRequest.of(page, size);
        Page<QuestionBank> questionPage = questionBankService.allQuestions(pageable);
        List<Subject> subjects = subjectService.getAllSubjects();
        List<Lesson> lessons = lessonService.getAllLessons();
        model.addAttribute("subjects", subjects);
        model.addAttribute("lessons", lessons);

        model.addAttribute("questionPage", questionPage);

        return "expert_manage_question";
    }

//    @GetMapping("/search_questions")
//    public String search_questions (
//            @RequestParam(value = "questionContent", required = false) String questionContent,
//            @RequestParam(value = "questionType", required = false) String questionType,
//            @RequestParam(defaultValue = "0") int page,  // Default to first page
//            @RequestParam(defaultValue = "15") int size,  // Default page size of 5
//            Model model) {
//        if(questionContent.trim().isEmpty() && questionType.trim().isEmpty()){
//            Pageable pageable = PageRequest.of(page, size);
//            Page<QuestionBank> questionPage = questionBankService.allQuestions(pageable);
//            model.addAttribute("questions", questionPage.getContent());
//            model.addAttribute("currentPage", page);
//            model.addAttribute("totalPages", questionPage.getTotalPages());
//        }else {
//            Pageable pageable = PageRequest.of(page, size);
//            Page<QuestionBank> questionPage = questionBankService.searchQuestion(pageable, questionContent, questionType);
//            model.addAttribute("questions", questionPage.getContent());
//            model.addAttribute("currentPage", page);
//            model.addAttribute("totalPages", questionPage.getTotalPages());
//        }
//
//        return "expert_manage_question";
//    }

    @GetMapping("/expert/expert_manage_lesson")
    public String getAllLessons(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 10; // 10 lessons per page
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Lesson> lessonPage = lessonService.getLessons(pageable);
        model.addAttribute("lessons", lessonPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", lessonPage.getTotalPages());

        return "expert_manage_lesson"; // Trả về trang HTML chứa danh sách lesson
    }

}

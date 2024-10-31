package com.quiz.g4.controller.expertManage;

import com.quiz.g4.entity.*;
import com.quiz.g4.repository.LessonRepository;
import com.quiz.g4.service.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    @Autowired
    private LessonRepository lessonRepository;

    @GetMapping("/expert/expert_dashboard")
    public String getDoashboard() {

        return "expert_dashboard";
    }

    //    @GetMapping("/expert/expert_quizz")
//    public String getExpertQuizz(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = null;
//        if (Objects.nonNull(authentication) && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
//            String email = authentication.getName();
//            user = userService.findByEmail(email);
//            model.addAttribute("user", user);
//            List<Quiz> quizzes = quizService.findQuizByAuther(user.getUserId());
//            model.addAttribute("quizzes", quizzes);
//
//            return "/quiz/expert_quizz";
//        }
//
//        List<Quiz> quizzes = quizService.findQuizByAuther(user.getUserId());
//        model.addAttribute("quizzes", quizzes);
//        return "/quiz/expert_quizz";
//    }
    @GetMapping("/expert/expert_quizz")
    public String getExpertQuizz(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;

        if (Objects.nonNull(authentication) && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            user = userService.findByEmail(email);
            model.addAttribute("user", user);

            Pageable pageable = PageRequest.of(page, size);
            Page<Quiz> quizzesPage = quizService.findQuizByAuthor(user.getUserId(), pageable);
            model.addAttribute("quizzes", quizzesPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", quizzesPage.getTotalPages());

            return "/quiz/expert_quizz";
        }

        // Handle case when user is null, if needed
        return "redirect:/login"; // or any appropriate action
    }


    @GetMapping("/expert/expert_manage_question")
    public String getAllQuestionsPaged(@RequestParam(defaultValue = "0") int page,  // Default to first page
                                       @RequestParam(defaultValue = "15") int size,  // Default page size of 5
                                       @RequestParam(defaultValue = "", required = false) String subjectFilter,
                                       Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<QuestionBank> questionPage = questionBankService.allQuestions(pageable);
        List<Subject> subjects = subjectService.getAllSubjects();
        model.addAttribute("subjects", subjects);
        model.addAttribute("selectedSubject", subjectFilter);
        if (!subjectFilter.isEmpty()) {
            List<Lesson> lessons = lessonService.getLessonsBySubjectId(subjectFilter);
            model.addAttribute("lessons", lessons);
            System.out.println(subjectFilter);
            System.out.println(lessons.get(1).getLessonName());
        }
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

//    @ResponseBody
//    @GetMapping("/expert/expert_manage_lesson/edit/{id}")
//    public ResponseEntity<?> editLesson(@PathVariable Integer id, @RequestBody Lesson updatedLesson) {
//
//        System.out.println("có" + updatedLesson);
//        Optional<Lesson> lesson = lessonRepository.findById(id);
//        if (lesson.isPresent()) {
//            Lesson existingLesson = lesson.get();
//            existingLesson.setLessonName(updatedLesson.getLessonName());
//            lessonRepository.save(existingLesson);
//            return ResponseEntity.ok(Collections.singletonMap("success", true));
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found");
//        }
//    }
//
//    @GetMapping("/expert/expert_manage_lesson/delete/{id}")
//    @ResponseBody
//    public ResponseEntity<?> deleteLesson(@PathVariable Integer id) {
//        System.out.println("có vào rồi" + id);
//        if (lessonRepository.existsById(id)) {
//            Lesson lesson=lessonRepository.findById(id).get();
//
//            lessonRepository.deleteById(id);
//            return ResponseEntity.ok(Collections.singletonMap("success", true));
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found");
//        }
//    }

}

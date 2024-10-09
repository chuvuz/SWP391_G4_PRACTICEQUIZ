package com.quiz.g4.controller;

import com.quiz.g4.entity.*;
import com.quiz.g4.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UserService userService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private QuestionBankService questionBankService;

    @Autowired
    private LessonResultService lessonResultService;


    // Endpoint: /quiz-list
    @GetMapping("/quiz-list")
    public String quizList(Model model,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "10") int size) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            model.addAttribute("user", user);  // Thêm thông tin người dùng vào model
        }

        // Lấy danh sách các subject và expert để hiển thị
        List<Subject> subjects = subjectService.getAllSubjects();
        List<User> experts = userService.findByRoleId(3); // Expert có role_id = 3
        model.addAttribute("subjects", subjects);
        model.addAttribute("experts", experts);

        // Lấy danh sách quiz
        Page<Quiz> quizPage = quizService.getAllQuizzess(page, size);
        model.addAttribute("quizPage", quizPage);

        return "quiz/quiz-list"; // Trả về view quiz-list
    }

    // Endpoint: /search-quizzes
    @GetMapping("/search-quizzes")
    public String searchQuizzes(Model model,
                                @RequestParam(value = "quizName", required = false) String quizName,
                                @RequestParam(value = "subjectId", required = false) Integer subjectId,
                                @RequestParam(value = "expertId", required = false) Integer expertId,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            model.addAttribute("user", user);  // Thêm thông tin người dùng vào model
        }

        // Lấy danh sách subject và expert để hiển thị trong form tìm kiếm
        List<Subject> subjects = subjectService.getAllSubjects();
        List<User> experts = userService.findByRoleId(3); // Expert có role_id = 3
        model.addAttribute("subjects", subjects);
        model.addAttribute("experts", experts);

        // Truyền giá trị đã chọn vào model để hiển thị selected trong dropdown
        model.addAttribute("selectedSubjectId", subjectId);
        model.addAttribute("selectedExpertId", expertId);
        model.addAttribute("quizName", quizName);

        // Tìm kiếm quiz dựa trên các tiêu chí
        Page<Quiz> quizPage = quizService.searchQuizzes(quizName, subjectId, expertId, page, size);
        model.addAttribute("quizPage", quizPage);

        return "quiz/quiz-list"; // Trả về view quiz-list cùng với kết quả tìm kiếm
    }


    @GetMapping("/quiz-detail/{quizId}")
    public String getQuizDetail(@PathVariable("quizId") Integer quizId, Model model) {
        // Lấy thông tin người dùng đã đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            model.addAttribute("user", user);  // Thêm thông tin người dùng vào model
        }

        // Lấy quiz với các bài học
        Quiz quiz = quizService.getQuizWithLesson(quizId);

        // Kiểm tra xem quiz có tồn tại không
        if (quiz == null) {
            return "error/404"; // Trả về trang lỗi nếu quiz không tồn tại
        }

        // Thêm quiz vào model để render ra view
        model.addAttribute("quiz", quiz);

        return "quiz/quiz-detail";  // Tên của view Thymeleaf để hiển thị chi tiết quiz
    }


    @GetMapping("/lesson-detail/{lessonId}")
    public String getLessonDetail(@PathVariable("lessonId") Integer lessonId, Model model) {
        // Lấy thông tin người dùng đã đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            model.addAttribute("user", user);  // Thêm thông tin người dùng vào model
        }

        // Lấy thông tin bài học và các câu hỏi
        Lesson lesson = lessonService.getLessonWithQuestions(lessonId);

        // Kiểm tra xem lesson có tồn tại không
        if (lesson == null) {
            return "error/404"; // Trả về trang lỗi nếu lesson không tồn tại
        }

        // Thêm bài học và câu hỏi vào model để render ra view
        model.addAttribute("lesson", lesson);

        return "quiz/lesson-detail";  // Tên của view Thymeleaf để hiển thị chi tiết bài học và câu hỏi
    }

    // Handle lesson answers submission
    @PostMapping("/lesson-submit")
    public String submitLessonAnswers(@RequestParam Map<String, String> formData, Model model) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        // Extract lessonId from the formData (you might pass this in a hidden input in the form)
        Integer lessonId = Integer.parseInt(formData.get("lessonId"));
        Lesson lesson = lessonService.getLessonWithQuestions(lessonId);

        // If lesson not found, return error
        if (lesson == null) {
            return "error/404";
        }

        // Initialize variables to track score
        double totalQuestions = lesson.getQuestionBanks().size();
        double correctAnswers = 0;

        // Iterate through the questions and check the answers
        for (QuestionBank question : lesson.getQuestionBanks()) {
            String selectedOptionId = formData.get(String.valueOf(question.getQuestionId()));
            if (selectedOptionId != null) {
                boolean isCorrect = questionBankService.checkCorrectAnswer(question.getQuestionId(), Integer.parseInt(selectedOptionId));
                if (isCorrect) {
                    correctAnswers++;
                }
            }
        }

        // Calculate score (percentage)
        double score = (correctAnswers / totalQuestions) * 100;

        // Save lesson result
        LessonResult lessonResult = new LessonResult();
        lessonResult.setUser(user);
        lessonResult.setLesson(lesson);
        lessonResult.setScore(score);
        lessonResult.setCompletedAt(LocalDateTime.now());

        lessonResultService.saveLessonResult(lessonResult);

        // Redirect to the lesson result page, passing the result ID
        return "redirect:/lesson-result/" + lessonResult.getResultId();
    }

    // Show the lesson result after submission
    @GetMapping("/lesson-result/{resultId}")
    public String showLessonResult(@PathVariable("resultId") Integer resultId, Model model) {
        // Fetch the lesson result using the resultId
        LessonResult lessonResult = lessonResultService.findLessonResultById(resultId);

        if (lessonResult == null) {
            return "error/404";  // Return error page if result is not found
        }

        // Add the lesson result to the model
        model.addAttribute("lessonResult", lessonResult);

        // Return the view to display the result
        return "quiz/lesson-result";
    }


}

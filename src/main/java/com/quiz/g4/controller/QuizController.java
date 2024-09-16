package com.quiz.g4.controller;

import com.quiz.g4.entity.Quiz;
import com.quiz.g4.entity.Subject;
import com.quiz.g4.entity.User;
import com.quiz.g4.service.QuizService;
import com.quiz.g4.service.SubjectService;
import com.quiz.g4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UserService userService;

    // Endpoint: /quiz-list
    @GetMapping("/quiz-list")
    public String quizList(Model model,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "10") int size) {

        // Lấy danh sách các subject và expert để hiển thị
        List<Subject> subjects = subjectService.getAllSubjects();
        List<User> experts = userService.findByRoleId(3); // Expert có role_id = 3
        model.addAttribute("subjects", subjects);
        model.addAttribute("experts", experts);

        // Lấy danh sách quiz
        Page<Quiz> quizPage = quizService.getAllQuizzes(page, size);
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

        // Lấy quiz với các câu hỏi và lựa chọn trả lời
        Quiz quiz = quizService.getQuizWithQuestionsAndAnswers(quizId);

        // Kiểm tra xem quiz có tồn tại không
        if (quiz == null) {
            return "error/404"; // Trả về trang lỗi nếu quiz không tồn tại
        }

        // Thêm quiz vào model để render ra view
        model.addAttribute("quiz", quiz);

        return "quiz/quiz-detail";  // Tên của view Thymeleaf để hiển thị chi tiết quiz
    }





}

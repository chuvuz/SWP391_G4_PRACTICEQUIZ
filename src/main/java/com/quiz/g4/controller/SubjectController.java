package com.quiz.g4.controller;

import com.quiz.g4.entity.*;
import com.quiz.g4.repository.AnswerOptionRepository;
import com.quiz.g4.repository.SubjectRepository;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class SubjectController {

    @Autowired
    private UserService userService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private LessonService lessonService;


    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @Autowired
    private QuizResultService quizResultService;

    @Autowired
    private UserAnswerService userAnswerService;

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/subject-list")
    public String quizList(Model model,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "9") int size) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            model.addAttribute("user", user);  // Thêm thông tin người dùng vào model
        }

        // Lấy danh sách Subject
        Page<Subject> subjectsPage = subjectService.getAllSubject(page, size);
        model.addAttribute("subjectsPage", subjectsPage);

        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);

        return "quiz/subject-list"; // Trả về view quiz-list
    }


    @GetMapping("/search-subject")
    public String searchQuizzes(Model model,
                                @RequestParam(value = "subjectName", required = false) String subjectName,
                                @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "9") int size) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            model.addAttribute("user", user);  // Thêm thông tin người dùng vào model
        }

        model.addAttribute("subjectName", subjectName);
        model.addAttribute("selectedCategoryId", categoryId);

        // Sử dụng `categoryId` để lọc theo danh mục nếu có
        Page<Subject> subjectsPage = subjectService.searchSubject(subjectName, categoryId, page, size);
        model.addAttribute("subjectsPage", subjectsPage);

        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);

        return "quiz/subject-list"; // Trả về view quiz-list cùng với kết quả tìm kiếm
    }


    @GetMapping("/subject-detail/{subjectId}")
    public String showSubjectDetail(@PathVariable("subjectId") Integer subjectId, Model model) {

        // Kiểm tra xác thực người dùng
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            model.addAttribute("user", user);  // Thêm thông tin người dùng vào model
        }

        // Lấy subject theo subjectId từ service
        Subject subject = subjectService.getSubjectById(subjectId);

        // Lấy danh sách Lesson theo subjectId, sắp xếp theo createdDate tăng dần
        List<Lesson> lessons = lessonService.getLessonsBySubjectIdWithCreateDateAsc(subjectId);

        // Đưa thông tin subject và danh sách lessons vào model
        model.addAttribute("subject", subject);
        model.addAttribute("lessons", lessons);

        return "quiz/subject-detail"; // Trả về template `subject-detail.html`
    }


    @GetMapping("/lesson-detail/{lessonId}")
    public String getLessonDetail(@PathVariable("lessonId") Integer lessonId, Model model) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            model.addAttribute("user", user);  // Thêm thông tin người dùng vào model
        }


        // Lấy danh sách quiz
        List<Quiz> quizzes = quizService.getQuizzesByLessonId(lessonId);
        // Thêm danh sách quiz vào model
        model.addAttribute("quizzes", quizzes);


        // Lấy thông tin lesson
        Lesson lesson = lessonService.getLessonById(lessonId);
        model.addAttribute("lesson", lesson);

        // Trả về view lesson-detail.html
        return "quiz/lesson-detail";
    }


}

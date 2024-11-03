package com.quiz.g4.controller.expertManage;

import com.quiz.g4.entity.*;
import com.quiz.g4.repository.LessonRepository;
import com.quiz.g4.repository.QuizRepository;
import com.quiz.g4.repository.SubjectRepository;
import com.quiz.g4.service.*;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

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
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private QuizRepository quizRepository;

    @GetMapping("/expert/expert_dashboard")
    public String getDoashboard() {

        return "expert_dashboard";
    }
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

            List<Subject> subjects = subjectService.getAllSubjects();
            model.addAttribute("subjects", subjects);


            return "/quiz/expert_quizz";
        }

        // Handle case when user is null, if needed
        return "redirect:/login"; // or any appropriate action
    }

    @GetMapping("/expert/api/questions")
    public String getQuestions(@RequestParam("subjectId") Integer subjectId,
                               @RequestParam("quizName") String quizName,
                               @RequestParam("description") String description,
                               Model model) {

        // Lấy danh sách câu hỏi dựa trên subjectId
        Subject subject = subjectService.getSubjectById(subjectId);
//        subjectRepository.findBySubjectId(subjectId);

        // Kiểm tra nếu môn học không tồn tại
        if (subject == null) {
            model.addAttribute("error", "Subject not found");
            return "error"; // hoặc trang lỗi của bạn
        }

        List<QuestionBank> questions = questionBankService.getQuestionsBySubjectId(subject);
        System.out.println("Danh sách câu hỏi đã trả về: " + questions.size());

        // Kiểm tra nếu không có câu hỏi
        if (questions.isEmpty()) {
            model.addAttribute("message", "No questions found for this subject.");
            return "noQuestions"; // hoặc trang hiển thị không có câu hỏi
        }

        //Lấy ra các lesstion của subject bằng subject id
        List<Lesson> lessons=lessonService.getLessonsBySubjectId(subjectId);
//        getLessonsBySubjectId(subjectId);

        // Thêm các thuộc tính vào model để hiển thị trong view
        model.addAttribute("subject", subject);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("questions", questions);
        model.addAttribute("quizName", quizName);
        model.addAttribute("description", description);
        model.addAttribute("lessons", lessons);

        return "select_questions"; // Tên của HTML mà bạn sẽ hiển thị các câu hỏi
    }


    @GetMapping("/expert/api/create-quiz")
    public String createQuiz(@RequestParam Integer subjectId,
                             @RequestParam String quizName,
                             @RequestParam String description,
                             @RequestParam List<Integer> selectedQuestions,
                             @RequestParam("lessonId") Integer lessonId,
                             Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (Objects.nonNull(authentication) && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            user = userService.findByEmail(email);
            model.addAttribute("user", user);

            // Tạo một đối tượng Quiz mới
            Quiz new_quiz = new Quiz();
            new_quiz.setQuizName(quizName);
            new_quiz.setDescription(description);
            new_quiz.setCreatedDate(LocalDate.now());
            new_quiz.setUpdatedDate(LocalDate.now());
            new_quiz.setCreatedBy(user);
            new_quiz.setIsActive(true);
            Subject subject = subjectRepository.findBySubjectId(subjectId);
            Lesson lesson = lessonRepository.findByLessonId(lessonId);
            new_quiz.setSubject(subject);
            new_quiz.setLesson(lesson);

            // Lấy danh sách câu hỏi từ selectedQuestions và chuyển đổi thành Set
            List<QuestionBank> questions = questionBankService.getQuestionsByIds(selectedQuestions);

            // Thêm các câu hỏi vào quiz
            new_quiz.setQuestions(new HashSet<>(questions)); // Chuyển đổi List thành Set


            // Lưu Quiz vào database
            quizRepository.save(new_quiz);
        }
        // Thêm các thuộc tính vào model để hiển thị thông báo hoặc chuyển tiếp đến view khác nếu cần
        model.addAttribute("quizName", quizName);
        model.addAttribute("description", description);
        model.addAttribute("message", "Quiz created successfully");

        return "expert_dashboard";// Trang HTML để xác nhận quiz đã được tạo thành công
    }

    @GetMapping("/expert/expert_manage_question")
    public String getAllQuestionsPaged(
            @RequestParam(defaultValue = "",required = false) String contentFilter,
            @RequestParam(defaultValue = "",required = false) String typeFilter,
            @RequestParam(defaultValue = "",required = false) Integer subjectFilter,
            @RequestParam(defaultValue = "",required = false) Integer lessonFilter,
            @RequestParam(defaultValue = "0") int page,  // Default to first page
            @RequestParam(defaultValue = "5") int size,  // Default page size of 5
            Model model){
        Pageable pageable = PageRequest.of(page, size);
        Page<QuestionBank> questionPage = questionBankService.searchQuestion(contentFilter,typeFilter,subjectFilter,lessonFilter,pageable);
        List<Subject> subjects = subjectService.getAllSubjects();
        model.addAttribute("subjects", subjects);
        model.addAttribute("selectedSubject", subjectFilter);
        if (subjectFilter != null) {
            List<Lesson> lessons = lessonService.getLessonsBySubjectId(subjectFilter);
            model.addAttribute("lessons", lessons);
        }else {
            List<Lesson> lessons = lessonService.getAllLessons();
            model.addAttribute("lessons", lessons);
        }

        model.addAttribute("questionPage", questionPage);
        model.addAttribute("subjectF", subjectFilter);
        model.addAttribute("typeF", typeFilter);
        model.addAttribute("lessonF", lessonFilter);
        model.addAttribute("contentF", contentFilter);

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

        List<Subject> subjects = subjectService.getAllSubjects();
        model.addAttribute("subjects", subjects);
        return "expert_manage_lesson"; // Trả về trang HTML chứa danh sách lesson
    }

    @PostMapping("/expert/create_lesson")
    public String createLesson(@RequestParam String lessonName, @RequestParam Integer subjectId) {

        Subject subject = subjectService.getSubjectById(subjectId);
        Lesson lesson = new Lesson();
        lesson.setLessonName(lessonName);
        lesson.setSubject(subject);
        lesson.setCreatedDate(LocalDate.now());
        lesson.setUpdatedDate(LocalDate.now());
        lessonRepository.save(lesson);
        // Implement logic to save the lesson
        // e.g., lessonService.save(new Lesson(lessonName, subject));

        return "redirect:/expert/expert_manage_lesson"; // Redirect to the lesson management page
    }

}

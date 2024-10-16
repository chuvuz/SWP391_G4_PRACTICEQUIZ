package com.quiz.g4.controller;

import com.quiz.g4.entity.*;
import com.quiz.g4.repository.AnswerOptionRepository;
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

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private AnswerOptionService answerOptionService;

    @Autowired
    private LessonResultService lessonResultService;

    @Autowired
    private UserAnswerService userAnswerService;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;


    // Endpoint: /quiz-list
    @GetMapping("/quiz-list")
    public String quizList(Model model,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "9") int size) {

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
                                @RequestParam(value = "size", defaultValue = "9") int size) {

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
    public String submitLessonAnswers(@RequestParam Integer lessonId,
                                      HttpServletRequest request,
                                      Model model) {
        // Lấy thông tin người dùng đã đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        // Kiểm tra lesson
        Lesson lesson = lessonService.getLessonWithQuestions(lessonId);
        if (lesson == null) {
            return "error/404";
        }

        double totalQuestions = lesson.getQuestionBanks().size();
        double correctAnswers = 0;

        // Tạo kết quả cho bài học
        LessonResult lessonResult = new LessonResult();
        lessonResult.setUser(user);
        lessonResult.setLesson(lesson);
        lessonResult.setScore(0.0);
        lessonResult.setCompletedAt(LocalDateTime.now());
        lessonResultService.saveLessonResult(lessonResult);

        // Xử lý từng câu hỏi và đáp án đã chọn
        for (QuestionBank question : lesson.getQuestionBanks()) {
            String paramName = "question_" + question.getQuestionId();
            String[] selectedOptionIds = request.getParameterValues(paramName);

            if (selectedOptionIds == null || selectedOptionIds.length == 0) {
                System.out.println("No answer selected for question: " + question.getQuestionId());
                continue;
            }

            // Tìm tất cả các đáp án đã chọn
            List<Integer> optionIds = Arrays.stream(selectedOptionIds)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            List<AnswerOption> selectedAnswers = answerOptionRepository.findAllById(optionIds);

            if ("single".equals(question.getQuestionType())) {
                AnswerOption selectedAnswer = selectedAnswers.get(0);
                boolean isCorrect = selectedAnswer.getIsCorrect();
                if (isCorrect) correctAnswers++;

                saveUserAnswer(lessonResult, question, selectedAnswer, isCorrect);

            } else if ("multi".equals(question.getQuestionType())) {
                List<AnswerOption> correctOptions = answerOptionRepository
                        .findCorrectOptionsByQuestionId(question.getQuestionId());

                Set<Integer> correctOptionIds = correctOptions.stream()
                        .map(AnswerOption::getOptionId)
                        .collect(Collectors.toSet());

                Set<Integer> selectedOptionSet = new HashSet<>(optionIds);

                boolean allCorrect = selectedOptionSet.equals(correctOptionIds);

                if (allCorrect) correctAnswers++;

                // Lưu từng đáp án đã chọn
                for (AnswerOption answer : selectedAnswers) {
                    boolean isCorrect = correctOptionIds.contains(answer.getOptionId());
                    saveUserAnswer(lessonResult, question, answer, isCorrect);
                }
            } else if ("numeric".equals(question.getQuestionType())) {
                // Lấy đáp án người dùng nhập
                String userInput = request.getParameter(paramName);

                try {
                    // Chuyển sang kiểu số
                    double userAnswer = Double.parseDouble(userInput);

                    // Tìm đáp án đúng từ cơ sở dữ liệu
                    AnswerOption correctAnswer = answerOptionRepository
                            .findCorrectOptionByQuestionId(question.getQuestionId());

                    // So sánh đáp án
                    boolean isCorrect = (Double.parseDouble(correctAnswer.getContent()) == userAnswer);

                    if (isCorrect) correctAnswers++;

                    // Lưu kết quả của người dùng
                    saveUserAnswer(lessonResult, question, correctAnswer, isCorrect);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid numeric input for question: " + question.getQuestionId());
                    // Xử lý trường hợp người dùng nhập sai định dạng số
                }
            }
        }

        // Tính điểm và lưu kết quả
        double score = (correctAnswers / totalQuestions) * 100;
        lessonResult.setScore(score);
        lessonResultService.saveLessonResult(lessonResult);

        // Chuyển hướng đến trang kết quả
        return "redirect:/lesson-result/" + lessonResult.getResultId();
    }

// Phương thức saveUserAnswer giữ nguyên không thay đổi

    private void saveUserAnswer(LessonResult lessonResult, QuestionBank question, AnswerOption answerOption, boolean isCorrect) {
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setLessonResult(lessonResult);
        userAnswer.setQuestion(question);
        userAnswer.setSelectedAnswer(answerOption);
        userAnswer.setIsCorrect(isCorrect);
        userAnswerService.saveUserAnswer(userAnswer);
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

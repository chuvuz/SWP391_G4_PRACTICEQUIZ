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
    public String submitLessonAnswers(@RequestParam("lessonId") Integer lessonId, @RequestParam Map<String, Object> formData, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        // Lấy lessonId từ @RequestParam
        Lesson lesson = lessonService.getLessonWithQuestions(lessonId);

        if (lesson == null) {
            return "error/404";
        }

        // In ra tổng số câu hỏi trong bài học
        double totalQuestions = lesson.getQuestionBanks().size();
        System.out.println("Tổng số câu hỏi: " + totalQuestions);

        double totalScore = 0;
        int correctCount = 0;  // Đếm số câu trả lời đúng
        int incorrectCount = 0;  // Đếm số câu trả lời sai

        LessonResult lessonResult = new LessonResult();
        lessonResult.setUser(user);
        lessonResult.setLesson(lesson);
        lessonResult.setCompletedAt(LocalDateTime.now());
        lessonResult.setScore(0.0);  // Khởi tạo điểm ban đầu
        lessonResultService.saveLessonResult(lessonResult);

        for (QuestionBank question : lesson.getQuestionBanks()) {
            Object answerObj = formData.get("question_" + question.getQuestionId());
            String[] selectedOptionIds;

            if (answerObj instanceof String[]) {
                selectedOptionIds = (String[]) answerObj;
            } else if (answerObj instanceof String) {
                selectedOptionIds = new String[]{(String) answerObj};
            } else {
                selectedOptionIds = new String[0];
            }

            if (selectedOptionIds.length > 0) {
                if ("single".equals(question.getQuestionType())) {
                    double result = processSingleChoiceQuestion(question, selectedOptionIds[0], lessonResult);
                    totalScore += result;
                    if (result > 0) {
                        correctCount++;  // Nếu đúng, tăng số câu đúng
                    } else {
                        incorrectCount++;  // Nếu sai, tăng số câu sai
                    }
                } else if ("multi".equals(question.getQuestionType())) {
                    double result = processMultiChoiceQuestion(question, selectedOptionIds, lessonResult);
                    totalScore += result;
                    if (result > 0) {
                        correctCount++;  // Nếu đúng, tăng số câu đúng
                    } else {
                        incorrectCount++;  // Nếu sai, tăng số câu sai
                    }
                }
            } else {
                processUnansweredQuestion(question, lessonResult);
                incorrectCount++;  // Nếu không trả lời câu hỏi, tính là sai
            }
        }

        // Tính tổng điểm phần trăm
        double finalScore = (totalScore / totalQuestions) * 100;
        lessonResult.setScore(finalScore);
        lessonResultService.saveLessonResult(lessonResult);

        // In ra số câu đúng, số câu sai và tổng điểm
        System.out.println("Số câu đúng: " + correctCount);
        System.out.println("Số câu sai: " + incorrectCount);
        System.out.println("Tổng điểm (phần trăm): " + finalScore);

        return "redirect:/lesson-result/" + lessonResult.getResultId();
    }



    private double processSingleChoiceQuestion(QuestionBank question, String selectedOptionId, LessonResult lessonResult) {
        AnswerOption selectedAnswer = answerOptionService.findById(Integer.parseInt(selectedOptionId));
        boolean isCorrect = selectedAnswer != null && selectedAnswer.getIsCorrect();

        saveUserAnswer(lessonResult, question, selectedAnswer, isCorrect);

        return isCorrect ? 1.0 : 0.0;
    }

    private double processMultiChoiceQuestion(QuestionBank question, String[] selectedOptionIds, LessonResult lessonResult) {
        // Lấy danh sách đáp án đúng cho câu hỏi multi-choice hiện tại
        List<AnswerOption> correctOptions = answerOptionService.findCorrectOptionsByQuestionId(question.getQuestionId());

        // Chuyển selectedOptionIds thành Set để dễ so sánh
        Set<String> selectedOptionsSet = new HashSet<>(Arrays.asList(selectedOptionIds));

        // Chuyển correctOptions thành Set để so sánh
        Set<String> correctOptionIds = correctOptions.stream()
                .map(option -> String.valueOf(option.getOptionId()))
                .collect(Collectors.toSet());

        // So sánh xem người dùng có chọn đúng tất cả đáp án mà không chọn thừa đáp án sai
        boolean allCorrect = selectedOptionsSet.equals(correctOptionIds);

        // Lưu mỗi đáp án mà người dùng đã chọn
        for (String selectedOptionId : selectedOptionsSet) {
            AnswerOption selectedAnswer = answerOptionService.findById(Integer.parseInt(selectedOptionId));
            boolean isCorrect = correctOptionIds.contains(selectedOptionId);  // Kiểm tra xem đáp án đã chọn có đúng không
            saveUserAnswer(lessonResult, question, selectedAnswer, isCorrect);
        }

        // Trả về 1.0 nếu tất cả các đáp án đều đúng, ngược lại trả về 0.0
        return allCorrect ? 1.0 : 0.0;
    }


    private void processUnansweredQuestion(QuestionBank question, LessonResult lessonResult) {
        // For unanswered questions, we might want to save a null answer or handle it differently
        saveUserAnswer(lessonResult, question, null, false);
    }

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

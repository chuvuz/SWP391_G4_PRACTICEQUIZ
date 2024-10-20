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

        return "quiz/subject-list"; // Trả về view quiz-list
    }


        @GetMapping("/search-subject")
    public String searchQuizzes(Model model,
                                @RequestParam(value = "subjectName", required = false) String subjectName,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "9") int size) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            model.addAttribute("user", user);  // Thêm thông tin người dùng vào model
        }

        model.addAttribute("subjectName", subjectName);


            Page<Subject> subjectsPage = subjectService.searchSubject(subjectName ,page, size);
            model.addAttribute("subjectsPage", subjectsPage);
            return "quiz/subject-list"; // Trả về view quiz-list cùng với kết quả tìm kiếm
    }

    @GetMapping("/subject-detail/{subjectId}")
    public String showSubjectDetail(@PathVariable("subjectId") Integer subjectId, Model model) {
        // Lấy subject theo subjectId từ service
        Subject subject = subjectService.getSubjectById(subjectId);

        // Đưa thông tin subject vào model
        model.addAttribute("subject", subject);

        return "quiz/subject-detail"; // Trả về template `subject-detail.html`
    }


    @GetMapping("/lesson-detail/{lessonId}")
    public String getLessonDetail(@PathVariable("lessonId") Integer lessonId, Model model) {

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


    @GetMapping("/quiz-detail/{quizId}")
    public String getQuizDetail(@PathVariable Integer quizId, Model model) {
        // Lấy quiz dựa trên quizId
        Quiz quiz = quizService.getQuizById(quizId);

        // Thêm quiz vào model để truyền qua Thymeleaf
        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", quiz.getQuestions());

        return "quiz/quiz-detail"; // Trả về tên view quiz-detail
    }

    @PostMapping("/quiz-submit")
    public String submitQuizAnswers(@RequestParam Integer quizId, HttpServletRequest request, Model model) {
        // Lấy thông tin người dùng đã đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        // Kiểm tra quiz
        Quiz quiz = quizService.getQuizWithQuestions(quizId);
        if (quiz == null) {
            return "error/404";
        }

        double totalQuestions = quiz.getQuestions().size();
        double correctAnswers = 0;

        // Tạo kết quả cho bài quiz
        QuizResult quizResult = new QuizResult();
        quizResult.setUser(user);
        quizResult.setQuiz(quiz);
        quizResult.setScore(0.0);
        quizResult.setCompletedAt(LocalDateTime.now());
        quizResultService.saveQuizResult(quizResult);

        // Xử lý từng câu hỏi và đáp án đã chọn
        for (QuestionBank question : quiz.getQuestions()) {
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

            // Xử lý câu hỏi đơn và đa lựa chọn
            if ("single".equals(question.getQuestionType())) {
                AnswerOption selectedAnswer = selectedAnswers.get(0);
                boolean isCorrect = selectedAnswer.getIsCorrect();
                if (isCorrect) correctAnswers++;

                saveUserAnswer(quizResult, question, selectedAnswer, isCorrect);

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
                    saveUserAnswer(quizResult, question, answer, isCorrect);
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
                    saveUserAnswer(quizResult, question, correctAnswer, isCorrect);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid numeric input for question: " + question.getQuestionId());
                }
            }
        }

        // Tính điểm và lưu kết quả
        double score = (correctAnswers / totalQuestions) * 100;
        score = Math.round(score * 100.0) / 100.0; // Làm tròn đến 2 chữ số sau dấu thập phân
        quizResult.setScore(score);
        quizResultService.saveQuizResult(quizResult);

        // Chuyển hướng đến trang kết quả
        return "redirect:/quiz-result/" + quizResult.getResultId();
    }

    // Phương thức saveUserAnswer giữ nguyên không thay đổi
    private void saveUserAnswer(QuizResult quizResult, QuestionBank question, AnswerOption answerOption, boolean isCorrect) {
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setQuizResult(quizResult);
        userAnswer.setQuestion(question);
        userAnswer.setSelectedAnswer(answerOption);
        userAnswer.setIsCorrect(isCorrect);
        userAnswerService.saveUserAnswer(userAnswer);
    }


    @GetMapping("/quiz-result/{resultId}")
    public String showQuizResult(@PathVariable("resultId") Integer resultId, Model model) {
        // Fetch the quiz result using the resultId
        QuizResult quizResult = quizResultService.findQuizResultById(resultId);

        if (quizResult == null) {
            return "error/404";  // Return error page if result is not found
        }

        // Add the quiz result to the model
        model.addAttribute("quizResult", quizResult);

        // Return the view to display the result
        return "quiz/quiz-result";
    }



}

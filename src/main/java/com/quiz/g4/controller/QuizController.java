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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class QuizController {

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @Autowired
    private QuizResultService quizResultService;

    @Autowired
    private UserAnswerService userAnswerService;


    @Autowired
    private UserService userService;

    @Autowired
    private QuizService quizService;


    @GetMapping("/quiz-detail/{quizId}")
    public String getQuizDetail(@PathVariable Integer quizId, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.nonNull(authentication) && authentication.isAuthenticated()
                && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);

            if (!user.isActive()) {
                // Logout người dùng nếu không hoạt động
                request.getSession().invalidate();
                SecurityContextHolder.clearContext();

                // Gửi thông báo lỗi và chuyển hướng tới trang login
                redirectAttributes.addFlashAttribute("errorMessage", "Your account has been disabled. Please contact administrator.");
                return "redirect:/login";
            }

            // Nếu người dùng hoạt động, thêm thông tin vào model
            model.addAttribute("user", user);
        }

        // Lấy quiz dựa trên quizId
        Quiz quiz = quizService.getQuizById(quizId);

        // Thêm quiz vào model để truyền qua Thymeleaf
        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", quiz.getQuestions());

        return "quiz/quiz-detail"; // Trả về tên view quiz-detail
    }

    // Định nghĩa phương thức xử lý POST request cho đường dẫn "/quiz-submit"
    @PostMapping("/quiz-submit")
    public String submitQuizAnswers(@RequestParam Integer quizId, HttpServletRequest request, Model model) {

        // Lấy thông tin người dùng đã đăng nhập thông qua Authentication từ Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Lấy email của người dùng hiện tại
        User user = userService.findByEmail(email); // Tìm đối tượng User theo email để lấy thông tin người dùng

        // Lấy thông tin quiz cùng các câu hỏi từ quizId
        Quiz quiz = quizService.getQuizWithQuestions(quizId);
        if (quiz == null) { // Nếu quiz không tồn tại, trả về trang lỗi 404
            return "error/404";
        }

        // Đếm tổng số câu hỏi trong quiz
        double totalQuestions = quiz.getQuestions().size();
        double correctAnswers = 0; // Biến đếm số lượng đáp án đúng của người dùng

        // Tạo đối tượng QuizResult để lưu kết quả bài quiz của người dùng
        QuizResult quizResult = new QuizResult();
        quizResult.setUser(user); // Gắn người dùng vào kết quả quiz
        quizResult.setQuiz(quiz); // Gắn quiz vào kết quả quiz
        quizResult.setScore(0.0); // Khởi tạo điểm với giá trị 0
        quizResult.setCompletedAt(LocalDateTime.now()); // Lưu thời gian hoàn thành quiz
        quizResultService.saveQuizResult(quizResult); // Lưu kết quả ban đầu vào cơ sở dữ liệu

        // Duyệt qua từng câu hỏi trong quiz để xử lý đáp án của người dùng
        for (QuestionBank question : quiz.getQuestions()) {
            String paramName = "question_" + question.getQuestionId(); // Tạo tên tham số cho câu hỏi
            String[] selectedOptionIds = request.getParameterValues(paramName); // Lấy các ID đáp án đã chọn từ request

            // Chuyển các ID đáp án từ kiểu String sang Integer để xử lý tiếp theo
            List<Integer> optionIds = Arrays.stream(selectedOptionIds)
                    .map(Integer::parseInt) // Chuyển đổi từng ID từ String sang Integer
                    .collect(Collectors.toList());

            // Tìm các đối tượng AnswerOption từ danh sách ID đã chọn
            List<AnswerOption> selectedAnswers = answerOptionRepository.findAllById(optionIds);

            // Xử lý câu hỏi loại lựa chọn đơn (chỉ chọn 1 đáp án đúng)
            if ("SINGLE_CHOICE".equals(question.getQuestionType())) {
                AnswerOption selectedAnswer = selectedAnswers.get(0); // Lấy đáp án đầu tiên
                boolean isCorrect = selectedAnswer.getIsCorrect(); // Kiểm tra xem đáp án có đúng không
                if (isCorrect) correctAnswers++; // Nếu đúng, tăng biến đếm số đáp án đúng

                // Lưu đáp án của người dùng vào hệ thống
                saveUserAnswer(quizResult, question, selectedAnswer, isCorrect);

            } else if ("MULTIPLE_CHOICE".equals(question.getQuestionType())) {
                // Nếu câu hỏi là loại lựa chọn nhiều đáp án
                List<AnswerOption> correctOptions = answerOptionRepository
                        .findCorrectOptionsByQuestionId(question.getQuestionId()); // Lấy các đáp án đúng từ cơ sở dữ liệu

                Set<Integer> correctOptionIds = correctOptions.stream()
                        .map(AnswerOption::getOptionId)
                        .collect(Collectors.toSet()); // Chuyển danh sách ID đáp án đúng thành Set để dễ so sánh

                Set<Integer> selectedOptionSet = new HashSet<>(optionIds); // Chuyển các ID đáp án đã chọn thành Set

                boolean allCorrect = selectedOptionSet.equals(correctOptionIds); // So sánh đáp án đã chọn với đáp án đúng

                if (allCorrect) correctAnswers++; // Nếu tất cả đáp án đúng, tăng biến đếm đáp án đúng

                // Lưu từng đáp án đã chọn vào hệ thống
                for (AnswerOption answer : selectedAnswers) {
                    boolean isCorrect = correctOptionIds.contains(answer.getOptionId()); // Kiểm tra xem đáp án có đúng không
                    saveUserAnswer(quizResult, question, answer, isCorrect); // Lưu đáp án vào hệ thống
                }

            } else if ("NUMERIC".equals(question.getQuestionType())) {
                // Nếu câu hỏi là loại trả lời số
                String userInput = request.getParameter(paramName); // Lấy đáp án người dùng nhập dưới dạng chuỗi

                try {
                    double userAnswer = Double.parseDouble(userInput); // Chuyển đáp án người dùng nhập sang kiểu double

                    // Lấy đáp án đúng từ cơ sở dữ liệu
                    AnswerOption correctAnswer = answerOptionRepository
                            .findCorrectOptionByQuestionId(question.getQuestionId());

                    // So sánh đáp án người dùng với đáp án đúng bằng cách so sánh giá trị số
                    boolean isCorrect = (Double.parseDouble(correctAnswer.getContent()) == userAnswer);

                    if (isCorrect) correctAnswers++; // Nếu đúng, tăng biến đếm đáp án đúng

                    // Lưu kết quả của người dùng
                    saveUserAnswer(quizResult, question, correctAnswer, isCorrect);

                } catch (NumberFormatException e) {
                    // Nếu người dùng nhập không đúng định dạng số, in ra thông báo lỗi
                    System.out.println("Invalid numeric input for question: " + question.getQuestionId());
                }
            }
        }

        // Tính điểm của người dùng (số đáp án đúng / tổng số câu hỏi) * 100
        double score = (correctAnswers / totalQuestions) * 100;
        score = Math.round(score * 100.0) / 100.0; // Làm tròn điểm đến 2 chữ số sau dấu thập phân
        quizResult.setScore(score); // Gắn điểm cho kết quả quiz
        quizResultService.saveQuizResult(quizResult); // Lưu kết quả quiz vào cơ sở dữ liệu

        // Chuyển hướng người dùng đến trang kết quả của quiz
        return "redirect:/quiz-result/" + quizResult.getResultId();
    }

    // Phương thức lưu đáp án của người dùng cho từng câu hỏi và kết quả kiểm tra
    private void saveUserAnswer(QuizResult quizResult, QuestionBank question, AnswerOption answerOption, boolean isCorrect) {
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setQuizResult(quizResult); // Gắn kết quả quiz vào đáp án người dùng
        userAnswer.setQuestion(question); // Gắn câu hỏi vào đáp án người dùng
        userAnswer.setSelectedAnswer(answerOption); // Gắn đáp án đã chọn vào đáp án người dùng
        userAnswer.setIsCorrect(isCorrect); // Gắn trạng thái đúng/sai vào đáp án người dùng
        userAnswerService.saveUserAnswer(userAnswer); // Lưu đáp án người dùng vào cơ sở dữ liệu
    }




    @GetMapping("/quiz-result/{resultId}")
    public String showQuizResult(@PathVariable("resultId") Integer resultId, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            model.addAttribute("user", user);  // Thêm thông tin người dùng vào model
        }

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

    @GetMapping("/quiz-review/{resultId}")
    public String reviewQuiz(@PathVariable("resultId") Integer resultId, Model model) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            model.addAttribute("user", user);  // Thêm thông tin người dùng vào model
        }

        // Get the quiz result by ID
        QuizResult quizResult = quizResultService.findQuizResultById(resultId);

        if (quizResult == null) {
            return "error/404";  // Redirect to error if result not found
        }

        // Get all user answers for this quiz result
        List<UserAnswer> userAnswers = userAnswerService.findByQuizResultId(resultId);

        // Add quiz result and user answers to the model
        model.addAttribute("quizResult", quizResult);
        model.addAttribute("userAnswers", userAnswers);

        return "quiz/quiz-review";  // View for quiz review
    }




}

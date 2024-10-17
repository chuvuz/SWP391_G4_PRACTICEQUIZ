package com.quiz.g4.controller.UserQuiz;

import com.quiz.g4.entity.Lesson;
import com.quiz.g4.entity.LessonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.quiz.g4.service.LessonService; // Import service để lấy bài học

import com.quiz.g4.service.LessonResultService; // Import service để lấy kết quả bài học


@Controller
public class QuizReviewController {

    @Autowired
    private LessonService lessonService; // Inject service bài học

    @Autowired
    private LessonResultService lessonResultService; // Inject service kết quả bài học

    // Hiển thị trang review quiz
    @GetMapping("/quiz_review/{resultId}")
    public String showQuizReview(@PathVariable("resultId") Integer resultId, Model model) {
        // Fetch the lesson result using the resultId
        LessonResult lessonResult = lessonResultService.findLessonResultById(resultId);

        if (lessonResult == null) {
            return "error/404"; // Return error page if result is not found
        }

        // Fetch the lesson associated with the lessonResult
        Lesson lesson = lessonService.getLessonById(lessonResult.getLessonId()); // Thay đổi này tùy thuộc vào cách lấy ID bài học

        if (lesson == null) {
            return "error/404"; // Return error page if lesson is not found
        }

        // Add the lesson and lesson result to the model
        model.addAttribute("lesson", lesson);
        model.addAttribute("lessonResult", lessonResult);

        // Return the view to display the quiz review
        return "quiz/quiz_review"; // Tên template chính xác, không cần thêm đuôi .html
    }
}

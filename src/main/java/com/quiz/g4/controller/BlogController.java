package com.quiz.g4.controller;

import com.quiz.g4.entity.Blog;
import com.quiz.g4.entity.Feedback;
import com.quiz.g4.entity.User;
import com.quiz.g4.enums.CommentStatus;
import com.quiz.g4.repository.BlogRepository;
import com.quiz.g4.repository.UserRepository;
import com.quiz.g4.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class BlogController {
    @Autowired
    private UserService userService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private QuizService quizService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/blog-list")
    public String getAllBlogs(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            model.addAttribute("user", user);
        }
        List<Blog> blogs = blogService.getAllBlogs();
        model.addAttribute("blogs", blogs);
        return "blog"; // tên của view (file Thymeleaf)
    }

    @GetMapping("/blogs/{id}")
    public String getBlogDetail(@PathVariable("id") Integer blogId, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            model.addAttribute("user", user);
        }

        Blog blog = blogRepository.getOne(blogId);
        model.addAttribute("blog", blog);

        List<Blog> blogs = blogService.getAllBlogs();
        model.addAttribute("blogs", blogs);

        List<Feedback> list_feedback= feedbackService.getFeedbackByBlogId(blogId);
        model.addAttribute("list_feedback", list_feedback);
        return "blog_detail";  // Tên template cho trang chi tiết blog
    }

    @PostMapping("/feedback")
    public String submitFeedback(@RequestParam("comments") String comments,
                                 @RequestParam("user_id") int user_id,
                                 @RequestParam("blog_id") int blog_id, // Chỉnh sửa ở đây
                                 Model model) {
        CommentStatus commentStatus = CommentStatus.PENDING;
        // Tạo đối tượng feedback và lưu vào cơ sở dữ liệu
        Feedback feedback = new Feedback();
        feedback.setBlogId(blog_id);
        feedback.setUser(userRepository.getOne(user_id));
        feedback.setComments(comments);
        feedback.setStatus(commentStatus);
        feedback.setCreatedAt(LocalDateTime.now());
        feedbackService.saveFeedback(feedback);

        // Trả về trang blog_detail với thông báo thành công
        model.addAttribute("message", "Feedback has been submitted successfully!");
        return "redirect:/blogs/" + blog_id;
    }

}
package com.quiz.g4.controller.authoController;

import com.quiz.g4.entity.User;
import com.quiz.g4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginForm(Authentication authentication, Model model, HttpSession session) {
        // Authentication đến từ Spring Security, chứa thông tin về người dùng hiện tại nếu họ đã đăng nhập
        if (authentication != null && authentication.isAuthenticated()) {
            // Nếu người dùng đã đăng nhập, chuyển hướng họ tới trang chủ
            return "redirect:/home";
        } else {
            // Trả về tên của template Thymeleaf "auth/login" để hiển thị trang đăng nhập
            return "auth/login";
        }
    }

    @PostMapping("/login")
    public String loginSubmit() {
            return "auth/login";
    }
    @GetMapping("/resetpassword")
    public String resetPassword() {
        return "auth/resetpassword";
    }
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User()); // Gửi đối tượng rỗng để Thymeleaf có thể binding dữ liệu
        return "auth/register"; // Trả về template Thymeleaf tương ứng
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.saveUser(user); // Lưu người dùng mới vào cơ sở dữ liệu
            model.addAttribute("successMessage", "Đăng ký thành công!");
            return "auth/login"; // Sau khi đăng ký thành công, chuyển đến trang đăng nhập
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Đã có lỗi xảy ra trong quá trình đăng ký.");
            return "auth/register"; // Trả về trang đăng ký nếu có lỗi
        }
    }


}

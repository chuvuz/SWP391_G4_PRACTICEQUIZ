    package com.quiz.g4.controller.authoController;

import com.quiz.g4.entity.User;
import com.quiz.g4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
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




    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());  // Add an empty User object to the model
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, @RequestParam("confirmPassword") String confirmPassword, Model model) {
        boolean hasError = false;

        // Kiểm tra Họ và Tên
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            model.addAttribute("fullNameError", "Họ và tên không được để trống!");
            hasError = true;
        } else if (!user.getFullName().matches("^[A-Za-zÀ-ỹ]+(?:\\s[A-Za-zÀ-ỹ]+)*$")) {
            model.addAttribute("fullNameError", "Tên không hợp lệ. Tên chỉ chứa chữ cái và một khoảng cách giữa các từ.");
            hasError = true;
        }


        // Kiểm tra Email
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            model.addAttribute("emailError", "Email không được để trống!");
            hasError = true;
        } else if (!user.getEmail().matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
            model.addAttribute("emailError", "Email phải có định dạng hợp lệ (ví dụ: example@example.com)!");
            hasError = true;
        } else if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("emailError", "Email đã được sử dụng!");
            hasError = true;
        }


        // Kiểm tra Mật Khẩu
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            model.addAttribute("passwordError", "Mật khẩu không được để trống!");
            hasError = true;
        } else if (!userService.isValidPassword(user.getPassword())) {
            model.addAttribute("passwordError", "Mật khẩu phải chứa ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và số, và không được chứa khoảng trắng!");
            hasError = true;
        }

        // Kiểm tra Nhập Lại Mật Khẩu
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("confirmPasswordError", "Mật khẩu và xác nhận mật khẩu không khớp!");
            hasError = true;
        }

        // Nếu có lỗi, quay lại trang đăng ký
        if (hasError) {
            model.addAttribute("user", user); // Gửi lại dữ liệu đã nhập để hiển thị
            return "auth/register";
        }

        // Lưu người dùng nếu không có lỗi
        try {
            userService.saveUser(user);
            model.addAttribute("successMessage", "Đăng ký thành công!");
            return "auth/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Đã có lỗi xảy ra trong quá trình đăng ký.");
            return "auth/register";
        }
    }


    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "auth/forgot-password";
    }

    //thực hiện quá trình reset
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try {
            userService.sendResetPasswordEmail(email, request);
            redirectAttributes.addFlashAttribute("successMessage", "Chúng tôi đã gửi Email về cho bạn");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Chúng tôi không thấy có Email người dùng ");
        }
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        if (!userService.isResetTokenValid(token)) {
            model.addAttribute("errorMessage", "Mã thông báo đặt lại không hợp lệ hoặc đã hết hạn.");
            return "auth/reset-password";
        }

        model.addAttribute("token", token);
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("password") String password,
                                       RedirectAttributes redirectAttributes) {
        if (password.length() < 6) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mật Khẩu phải dài ít nhất 6 ký tự.");
            return "redirect:/reset-password?token=" + token;
        }

        try {
            userService.updatePasswordReset(token, password);
            redirectAttributes.addFlashAttribute("successMessage", "Đặt lại Mật Khẩu thành công.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage" + e.getMessage());
            return "redirect:/reset-password?token=" + token;
        }
    }



}

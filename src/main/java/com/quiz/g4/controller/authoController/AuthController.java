package com.quiz.g4.controller.authoController;

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
    public String loginSubmit(@RequestParam("email") String email,
                              @RequestParam("password") String password,
                              Model model) {
        try {
            // Sử dụng UserService để tải thông tin người dùng từ cơ sở dữ liệu bằng email
            UserDetails userDetails = userService.loadUserByUsername(email);

            // Kiểm tra xem người dùng có tồn tại hay không
            if (userDetails == null) {
                model.addAttribute("errorMessage", "Email không tồn tại");
                return "auth/login";  // Trả về trang đăng nhập kèm theo thông báo lỗi
            }

            // Tạo đối tượng Authentication để xác thực người dùng
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

            // Đặt thông tin xác thực vào SecurityContextHolder để Spring Security xử lý
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Chuyển hướng người dùng đến trang chủ sau khi đăng nhập thành công
            return "redirect:/home";

        } catch (BadCredentialsException e) {
            // Xử lý lỗi khi mật khẩu không đúng
            model.addAttribute("errorMessage", "Mật khẩu không đúng");
            return "auth/login";  // Trả về trang đăng nhập kèm theo thông báo lỗi
        } catch (Exception e) {
            // Xử lý các lỗi khác nếu có
            model.addAttribute("errorMessage", "Đã xảy ra lỗi. Vui lòng thử lại.");
            return "auth/login";  // Trả về trang đăng nhập kèm theo thông báo lỗi
        }
    }
}

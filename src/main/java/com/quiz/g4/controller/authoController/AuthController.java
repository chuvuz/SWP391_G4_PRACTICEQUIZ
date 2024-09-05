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
    public String loginForm(Authentication authentication,
                            Model model,
                            HttpSession session) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home";
        } else {
            if (session.getAttribute("verificationSuccessMessage") != null) {
                model.addAttribute("successMessage", session.getAttribute("verificationSuccessMessage"));
                session.removeAttribute("verificationSuccessMessage");
            }
            return "auth/login";
        }
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam("email") String email,
                              @RequestParam("password") String password,
                              HttpSession session,
                              Model model) {
        try {
            UserDetails userDetails = userService.loadUserByUsername(email);

            if (userDetails == null) {
                model.addAttribute("errorMessage", "Email không tồn tại");
                return "auth/login";
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return "redirect:/home";

        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", "Mật khẩu không đúng");
            return "auth/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Đã xảy ra lỗi. Vui lòng thử lại.");
            return "auth/login";
        }
    }
}

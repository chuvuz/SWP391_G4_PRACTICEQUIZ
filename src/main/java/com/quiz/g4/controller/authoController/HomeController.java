package com.quiz.g4.controller.authoController;

import com.quiz.g4.entity.User;
import com.quiz.g4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller // Đảm bảo sử dụng @Controller thay vì @Service
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping({"/", "/home"})
    public String getCanteens(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (Objects.nonNull(authentication) && authentication.isAuthenticated()) {
            String email = authentication.getName();
            user = userService.findByEmail(email);
            if (Objects.nonNull(user)) {
                model.addAttribute("user", user);
            }
        }

        session.setAttribute("user", user);
        return "home"; // Trả về trang home.html từ thư mục templates
    }

}

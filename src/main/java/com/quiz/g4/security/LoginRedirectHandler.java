package com.quiz.g4.security;

import com.quiz.g4.entity.User;
import com.quiz.g4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginRedirectHandler implements AuthenticationSuccessHandler {


    @Autowired
    private UserService userService;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            response.sendRedirect("/home");
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EXPERT"))) {
            // Lấy thông tin user từ Authentication
            String email = authentication.getName();

            // Lấy user từ cơ sở dữ liệu (giả sử bạn có userService để tìm user theo email)
            User user = userService.findByEmail(email);

            // Kiểm tra subject_id và điều hướng tới trang phù hợp
            if (user.getSubject() != null) {
                switch (user.getSubject().getSubjectId()) {
                    case 1:
                        response.sendRedirect("/quiz-list/it");  // IT quizzes
                        break;
                    case 2:
                        response.sendRedirect("/quiz-list/economics");  // Economics quizzes
                        break;
                    case 3:
                        response.sendRedirect("/quiz-list/tourism");  // Tourism quizzes
                        break;
                    case 4:
                        response.sendRedirect("/quiz-list/design");  // Graphic Design quizzes
                        break;
                    case 5:
                        response.sendRedirect("/quiz-list/language");  // International Language quizzes
                        break;
                    default:
                        response.sendRedirect("/quiz-list");  // Default page if no subject_id found
                        break;
                }
            } else {
                response.sendRedirect("/quiz-list");  // Không có subject, về trang chung
            }
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MARKETING"))) {
            response.sendRedirect("/home");
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {
            response.sendRedirect("/home");
        } else {
            response.sendRedirect("/home");
        }
    }


}
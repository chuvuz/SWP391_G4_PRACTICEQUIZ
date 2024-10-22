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
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            if (user.isActive() && user.getRole().getRoleName().equalsIgnoreCase("ROLE_ADMIN")){
                response.sendRedirect("/admin/dashboard");
            }
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EXPERT"))) {
            // Lấy thông tin user từ Authentication
            String email = authentication.getName();

            // Lấy user từ cơ sở dữ liệu (giả sử bạn có userService để tìm user theo email)
            User user = userService.findByEmail(email);

            // Kiểm tra subject_id và điều hướng tới trang phù hợp
            response.sendRedirect("/expert/expert_dashboard");
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MARKETING"))) {
            response.sendRedirect("/home");
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {
            response.sendRedirect("/home");
        } else {
            response.sendRedirect("/home");
        }
    }


}

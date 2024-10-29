package com.quiz.g4.security;

import com.quiz.g4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginRedirectHandler loginRedirectHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/register/**", "/login", "/quiz-list", "/subject-list/**", "/search-quizzes/**",
                        "/search-subject", "/expert/**", "/lesson-detail/*", "/subject-detail/**",
                        "/home", "/css/**", "/js/**", "/vendors/**", "/images/**", "/blog-detail/**",
                        "/about/**", "/forgot-password/**", "/reset-password/**", "/expert_detail/**",
                        "/search_expert/**", "/blogs/**", "/blog-list", "/blogs/**", "/blog-list"
                        ).permitAll() // Cho phép truy cập vào tài nguyên tĩnh
                //cho 4 Role
                .antMatchers("/profile/**", "/quiz-review/**", "/quiz-submit/**", "/quiz-result/**", "/quiz-detail/**"
                        ).hasAnyRole("ADMIN", "EXPERT", "CUSTOMER", "MARKETING")

                //Role cho Admin

                .antMatchers("/manage-expert", "/manage-subject", "/edit-subject/**", "/admin/dashboard",
                        "/admin/users/create", "admin/users" ,"/manage_expert").hasRole("ADMIN")

                //Role cho Expert

                .antMatchers("/expert/**", "/QuestionBank/question_bank", "/questions", "/answers/**", "/search_questions",
                        "/questionlist", "/add_Question", "/QuestionBank/addQuestion", "/create",
                        "/questions/create", "/createQuestions", "/question/**", "/QuestionBank/UpdateQuestion",
                        "/updateQuestion/**", "/question/**").hasAnyRole("EXPERT")
                //Role cho Customer
                
                //Role cho Marketing
                .antMatchers("/marketing/dashboard","/marketing/customers","/marketing/customers/update-status").hasRole("MARKETING")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(loginRedirectHandler)  // Sử dụng success handler để xử lý chuyển hướng
                .failureHandler((request, response, exception) -> response.sendRedirect("/login?error=true"))
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll();
    }


}

package com.quiz.g4.controller.authoController;

import com.quiz.g4.entity.User;
import com.quiz.g4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getName().equals("anonymousUser")) {
            // If already logged in, check user status
            String email = authentication.getName();
            User user = userService.findByEmail(email);

            if (!user.isActive()) {
                // Logout if the account is disabled
                session.invalidate();
                SecurityContextHolder.clearContext();

                // Redirect to login page with error message
                model.addAttribute("errorMessage", "Your account has been disabled. Please contact the administrator.");
                return "auth/login"; // Return to login page with message
            }

            // If account is active, redirect to home page
            return "redirect:/home";
        }

        // If not logged in, display login page
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());  // Add an empty User object to the model
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,
                               @RequestParam("confirmPassword") String confirmPassword,
                               Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        boolean hasError = false;

        // Check Full Name
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            model.addAttribute("fullNameError", "Full name cannot be empty!");
            hasError = true;
        } else if (!user.getFullName().matches("^[A-Za-zÀ-ỹ]+(?:\\s[A-Za-zÀ-ỹ]+)*$")) {
            model.addAttribute("fullNameError", "Invalid name. Name can only contain letters and a space between words.");
            hasError = true;
        }

        // Check Email
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            model.addAttribute("emailError", "Email cannot be empty!");
            hasError = true;
        } else if (!user.getEmail().matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
            model.addAttribute("emailError", "Email must be in a valid format (e.g., example@example.com)!");
            hasError = true;
        } else if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("emailError", "Email has already been used!");
            hasError = true;
        }

        // Check Password
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            model.addAttribute("passwordError", "Password cannot be empty!");
            hasError = true;
        } else if (!userService.isValidPassword(user.getPassword())) {
            model.addAttribute("passwordError", "Password must contain at least 8 characters, including uppercase, lowercase letters, and numbers, and must not contain spaces!");
            hasError = true;
        }

        // Check Confirm Password
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("confirmPasswordError", "Password and confirmation password do not match!");
            hasError = true;
        }

        // If there are errors, return to the registration page
        if (hasError) {
            model.addAttribute("user", user); // Return entered data to display
            return "auth/register";
        }

        // Save user if no errors
        try {
            userService.saveUser(user);

            // Store email in session after successful registration
            session.setAttribute("registeredEmail", user.getEmail());

            // Add registration success message
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please log in.");

            // Redirect to login page
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred during registration.");
            return "auth/register";
        }
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "auth/forgot-password";
    }

    // Perform the reset process
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try {
            userService.sendResetPasswordEmail(email, request);
            redirectAttributes.addFlashAttribute("successMessage", "We have sent an email to you.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "We could not find a user with that email.");
        }
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        if (!userService.isResetTokenValid(token)) {
            model.addAttribute("errorMessage", "The reset token is invalid or has expired.");
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
            redirectAttributes.addFlashAttribute("errorMessage", "Password must be at least 6 characters long.");
            return "redirect:/reset-password?token=" + token;
        }

        try {
            userService.updatePasswordReset(token, password);
            redirectAttributes.addFlashAttribute("successMessage", "Password reset successful.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/reset-password?token=" + token;
        }
    }
}

package com.quiz.g4.controller;
import com.quiz.g4.dto.PasswordForm;
import com.quiz.g4.entity.User;
import com.quiz.g4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Random;

@Controller

public class UserProfileController {
    @Autowired
    private UserService userService;

    private final Path imagePath = Paths.get("user-images");

    @GetMapping("/profile")
    //Principal tra ve current user
    public String getUserProfile(Principal principal, Model model) {
        try {
            String username = principal.getName();

            User user = (User) userService.loadUserByUsername(username);
            model.addAttribute("user", user);
            return "profile";
        }catch (Exception e){
            return e.getMessage();
        }
    }
    @GetMapping("/profile/edit")
    public String editUserProfile(Model model, Authentication authentication) {
        String email = authentication.getName();
        try {
            User user = userService.findByEmail(email);
            model.addAttribute("user", user);
            return "editProfile";
        } catch (Exception e) {
            return "error";
        }
    }

    @PostMapping("/profile/edit")
    public String updateUserProfile(@ModelAttribute User updatedUser, Authentication authentication) {
        String email = authentication.getName();
        try {
            userService.updateUser(email, updatedUser);
            return "redirect:/profile";
        } catch (Exception e) {
            return "redirect:/profile";
        }
    }

    @GetMapping("/profile/change-password")
    public String changePasswordForm(Model model) {
        model.addAttribute("passwordForm", new PasswordForm());
        return "changePassword";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(@ModelAttribute PasswordForm passwordForm, Authentication authentication, Model model) {
        String email = authentication.getName();
        try {
            userService.changePassword(email, passwordForm.getNewPassword());
            return "redirect:/profile";
        } catch (IllegalArgumentException e) {
            model.addAttribute("passwordForm", passwordForm);
            return "changePassword";
        }
    }

    @PostMapping("/profile/upload")
    public String uploadProfileImage(@RequestParam("image") MultipartFile image, Model model, Principal principal) {

        String username = principal.getName();
        User user = (User) userService.loadUserByUsername(username);

        if (user != null && !image.isEmpty()) {
            try {
                String filename = "user-" + user.getUserId() + "-" + image.getOriginalFilename();
                Path filePath = imagePath.resolve(filename);
                Files.copy(image.getInputStream(), filePath);
                user.setProfileImage(filename);
                userService.updateUser(username, user);
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/profile";
            }
        }
        model.addAttribute("user", user);
        return "redirect:/profile";
    }
    @GetMapping("/profile/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path file = imagePath.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


}
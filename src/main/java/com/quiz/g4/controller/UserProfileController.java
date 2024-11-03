package com.quiz.g4.controller;

import com.quiz.g4.dto.PasswordForm;
import com.quiz.g4.entity.User;
import com.quiz.g4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Controller

public class UserProfileController {
    @Autowired
    private UserService userService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/profile")
    //Principal tra ve current user
    public String getUserProfile(Principal principal, Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (Objects.nonNull(authentication) && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
                String email = authentication.getName();
                User user = userService.findByEmail(email);
                model.addAttribute("user", user);  // Thêm thông tin người dùng vào model
            }
            model.addAttribute("passwordForm", new PasswordForm());
            return "profile";
        } catch (Exception e) {
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
    public String updateUserProfile(@ModelAttribute User updatedUser,
                                    @RequestParam(value = "image", required = false) MultipartFile image
                                    , Authentication authentication) {
        String email = authentication.getName();
        User user = (User) userService.loadUserByUsername(email);

        if (user != null && image != null && !image.isEmpty()) {
            updatedUser.setProfileImage(saveImage(image));
        }
        userService.updateUser(email, updatedUser);
        return "redirect:/profile";
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

//    @PostMapping("/profile/upload")
//    public String uploadProfileImage(@RequestParam("image") MultipartFile image, Model model, Principal principal) {
//
//        String username = principal.getName();
//        User user = (User) userService.loadUserByUsername(username);
//
//        if (user != null && !image.isEmpty()) {
//            try {
//                String filename = "user-" + user.getUserId() + "-" + image.getOriginalFilename();
//                Path filePath = imagePath.resolve(filename);
//                Files.copy(image.getInputStream(), filePath);
//                user.setProfileImage(filename);
//                userService.updateUser(username, user);
//            } catch (IOException e) {
//                e.printStackTrace();
//                return "redirect:/profile";
//            }
//        }
//        model.addAttribute("user", user);
//        return "redirect:/profile";
//    }

//    @GetMapping("/profile/images/{filename:.+}")
//    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
//        try {
//            Path file = imagePath.resolve(filename);
//            Resource resource = new UrlResource(file.toUri());
//
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                    .body(resource);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }

    @PostMapping("/profile/update-picture")
    public String updateProfilePicture(@RequestParam("profileImage") String profileImage, Principal principal) {
        userService.updateProfilePicture(principal.getName(), profileImage);
        return "redirect:/profile";
    }

    public String saveImage(MultipartFile image) {
        try {
            String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
            Path path = Paths.get(uploadDir, fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, image.getBytes());
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image", e);
        }
    }
}
package com.quiz.g4.controller.adminManage;

import com.quiz.g4.entity.Subject;
import com.quiz.g4.entity.User;
import com.quiz.g4.repository.SubjectRepository;
import com.quiz.g4.service.UserService;
import com.quiz.g4.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

@Controller
public class SubjectManagementController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UserService userService;

    @Autowired
    private SubjectRepository subjectRepository;

    // Đường dẫn để lưu trữ ảnh
    private final Path imagePath = Paths.get("subject-images");

    @GetMapping("/manage-subject")
    public String manageSubject(Model model, Principal principal,
                                @RequestParam(value = "subjectName", required = false) String subjectName,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "9") int size) {
        // Lấy thông tin người dùng đã đăng nhập thông qua principal
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            model.addAttribute("user", user); // Truyền thông tin người dùng vào model
        }

        Page<Subject> subjectsPage;
        if (subjectName != null && !subjectName.trim().isEmpty()) {
            // Nếu có subjectName, thực hiện tìm kiếm theo subjectName
            subjectsPage = subjectService.searchSubject(subjectName, page, size);
        } else {
            // Nếu không có subjectName, hiển thị tất cả các môn học
            subjectsPage = subjectService.getAllSubjectNoCondition(page, size);
        }

        model.addAttribute("subjectsPage", subjectsPage);
        model.addAttribute("subjectName", subjectName); // Truyền subjectName vào model để giữ giá trị trong ô tìm kiếm

        // Trả về trang admin/manage-subject
        return "admin/manage-subject";
    }


    @PostMapping("/create-subject")
    public String createSubject(@RequestParam("subjectName") String subjectName,
                                @RequestParam("isActive") boolean isActive,
                                @RequestParam("imageUrl") String imageUrl,  // Nhận URL ảnh từ form
                                Model model, Principal principal) {
        // Kiểm tra nếu URL ảnh trống
        if (imageUrl == null || imageUrl.isEmpty()) {
            model.addAttribute("error", "Image URL is required.");
            return "redirect:/manage-subject";
        }

        // Kiểm tra người dùng
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            model.addAttribute("user", user);
        }

        // Tạo môn học mới với URL ảnh
        subjectService.createSubjectWithImageUrl(subjectName, isActive, imageUrl);
        return "redirect:/manage-subject";
    }


    @GetMapping("/edit-subject/{subjectId}")
    public String editSubjectForm(Model model, @PathVariable Integer subjectId, Principal principal) {
        // Kiểm tra xem người dùng có đăng nhập hay không
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            model.addAttribute("user", user);
        }

        // Lấy thông tin subject theo subjectId
        Subject subject = subjectService.findById(subjectId);
        model.addAttribute("subject", subject);

        return "admin/edit-subject";
    }

    @PostMapping("/edit-subject")
    public String updateSubject(@RequestParam("subjectId") int id,
                                @RequestParam("subjectName") String subjectName,
                                @RequestParam("isActive") boolean isActive,
                                @RequestParam(value = "imageUrl", required = false) String imageUrl  // Nhận URL ảnh từ form
    ) {
        // Nếu không có URL ảnh mới, giữ nguyên URL ảnh cũ
        subjectService.updateSubjectWithImageUrl(id, subjectName, isActive, imageUrl);
        return "redirect:/manage-subject";
    }


    @GetMapping("/subject/images/{filename:.+}")
    public ResponseEntity<Resource> getSubjectImage(@PathVariable String filename) {
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

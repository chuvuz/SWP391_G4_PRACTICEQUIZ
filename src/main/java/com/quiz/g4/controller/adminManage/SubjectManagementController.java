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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
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
            subjectsPage = subjectService.searchSubjectAll(subjectName, page, size);
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
                                @RequestParam("imageUrl") String imageUrl, RedirectAttributes redirectAttributes,// Nhận URL ảnh từ form
                                Model model, Principal principal) {
        // Kiểm tra nếu URL ảnh trống
        if (imageUrl == null || imageUrl.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Image URL is required.");
            return "redirect:/admin/manage-subject";
        } // Check subjectName format
         if (!subjectName.matches("[A-Za-zA-ÿ]+(?:\\s[A-Za-zA-ÿ]+)*$")) {
             redirectAttributes .addFlashAttribute("error", "Invalid subject name format.");
            return "redirect:/admin/manage-subject";
        }

        // Kiểm tra người dùng
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            model.addAttribute("user", user);
        }
        if (subjectService.existsBySubjectName(subjectName)) {
            redirectAttributes.addFlashAttribute("error", "Subject name already exists.");
            return "redirect:/admin/manage-subject"; // Trở về trang quản lý môn
             }

        // Tạo môn học mới với URL ảnh
        subjectService.createSubjectWithImageUrl(subjectName, isActive, imageUrl);
        return "redirect:/admin/manage-subject";
    }


    @GetMapping("/manage-subject/edit-subject/{subjectId}")
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

    @PostMapping("/manage-subject/edit-subject")
    public String updateSubject(@RequestParam("subjectId") int id,
                                @RequestParam("subjectName") String subjectName,
                                @RequestParam("isActive") boolean isActive,
                                @RequestParam(value = "imageUrl", required = false) String imageUrl,  // Nhận URL ảnh từ form
                                Model model, RedirectAttributes redirectAttributes
// Nhận URL ảnh từ form
    ) {
        Subject existingSubject = subjectService.findBySubjectId(id);
// Kiểm tra định dạng subjectName
        if (!subjectName.matches("^[A-Za-zÀ-ỹ]+(?:\\s[A-Za-zÀ-ỹ]+)*$")) {
            redirectAttributes.addFlashAttribute("error", "Invalid subject name format.");
            return "redirect:/admin/manage-subject/edit-subject/" + id;
        }
        // Kiểm tra trùng tên môn học và tên khác với môn học hiện tại
        if(subjectService.existsBySubjectName(subjectName) &&
                (existingSubject == null || !existingSubject.getSubjectName().equals(subjectName))) {
            redirectAttributes.addFlashAttribute("error", "Subject name already exists.");
            return "redirect:/admin/manage-subject/edit-subject/"+id; // Hoặc trả về trang chỉnh sửa với thông báo lỗi
        }
        // Nếu không có URL ảnh mới, giữ nguyên URL ảnh cũ
        subjectService.updateSubjectWithImageUrl(id, subjectName, isActive, imageUrl);
        return "redirect:/admin/manage-subject";
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

package com.quiz.g4.controller.adminManage;

import com.quiz.g4.entity.Category;
import com.quiz.g4.entity.Subject;
import com.quiz.g4.entity.User;
import com.quiz.g4.repository.SubjectRepository;
import com.quiz.g4.service.CategoryService;
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
import java.util.List;

@Controller
@RequestMapping("/admin")
public class SubjectManagementController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UserService userService;

    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private CategoryService categoryService;


    // Đường dẫn để lưu trữ ảnh
    private final Path imagePath = Paths.get("subject-images");

    @GetMapping("/manage-subject")
    public String manageSubject(Model model, Principal principal,
                                @RequestParam(value = "subjectName", required = false) String subjectName,
                                @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "9") int size) {

        // Lấy tất cả các category để hiển thị trong dropdown


        // Lấy thông tin người dùng đã đăng nhập thông qua principal
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            model.addAttribute("user", user); // Truyền thông tin người dùng vào model
        }

        model.addAttribute("subjectName", subjectName);
        model.addAttribute("selectedCategoryId", categoryId);

        // Sử dụng `categoryId` để lọc theo danh mục nếu có
        Page<Subject> subjectsPage = subjectService.searchSubjectAll(subjectName, categoryId, page, size);
        model.addAttribute("subjectsPage", subjectsPage);

        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);

        // Trả về trang admin/manage-subject
        return "admin/manage-subject";
    }


    @PostMapping("/create-subject")
    public String createSubject(@RequestParam("subjectName") String subjectName,
                                @RequestParam("isActive") boolean isActive,
                                @RequestParam("categoryId") Integer categoryId, // Thêm categoryId
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
        subjectService.createSubjectWithImageUrl(subjectName,categoryId, isActive, imageUrl);
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
       // model.addAttribute("categories", categoryService.getAllCategory());
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);
        return "admin/edit-subject";
    }

    @PostMapping("/manage-subject/edit-subject")
    public String updateSubject(@RequestParam("subjectId") int id,
                                @RequestParam("subjectName") String subjectName,
                                @RequestParam("isActive") boolean isActive,
                                @RequestParam("categoryId") Integer categoryId,
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
        subjectService.updateSubjectWithImageUrl(id, subjectName, isActive, imageUrl, categoryId);
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

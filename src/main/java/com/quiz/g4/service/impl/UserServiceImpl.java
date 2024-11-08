package com.quiz.g4.service.impl;

import com.quiz.g4.entity.ResetToken;
import com.quiz.g4.entity.Role;

import com.quiz.g4.entity.User;
import com.quiz.g4.repository.ResetTokenRepository;
import com.quiz.g4.repository.RoleRepository;
import com.quiz.g4.repository.UserRepository;
import com.quiz.g4.service.UserService;
import com.quiz.g4.utils.UrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ResetTokenRepository resetTokenRepository;

    @Autowired
    private JavaMailSender mailSender;


    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserByUserId(Integer userId) {
        return userRepository.findUserByUserId(userId);
    }

    @Override
    public List<User> findByRoleId(int roleId) {
        return userRepository.findByRoleRoleId(roleId);
    }

    @Override
    public Page<User> getAllExpert(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findByRoleRoleId(3, pageable);
    }

//    @Override
//    public Page<User> searchExpert(String expertName, Integer subjectId, Integer roleId, int page, int size){
//        Pageable pageable = PageRequest.of(page, size);
//        return userRepository.searchExpert(expertName, subjectId, roleId, pageable);
//    }

    @Override
    public void saveUser(User user) {


        // Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set other fields like fullName và email
        user.setFullName(user.getFullName());
        user.setEmail(user.getEmail());
        // Set phone, dateOfBirth, and gender fields
        user.setPhone(user.getPhone());
        user.setDateOfBirth(user.getDateOfBirth());
        user.setGender(user.getGender());
        user.setIsActive(true);


        // Kiểm tra role người dùng
        Role role = roleRepository.findRoleByRoleId(1);
        if (role == null) {
            throw new IllegalArgumentException("Invalid role");
        } else {
            user.setRole(role);
        }

        // Lưu người dùng vào cơ sở dữ liệu
        userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return user;
    }

    @Override
    public User updateUser(String email, User updatedUser) {
        User user = findByEmail(email);
        user.setFullName(updatedUser.getFullName());

        user.setProfileImage(updatedUser.getProfileImage());
        if(updatedUser.getProfileImage() != null){
            user.setProfileImage(updatedUser.getProfileImage());
        }
        user.setDescription(updatedUser.getDescription());
        user.setDateOfBirth(updatedUser.getDateOfBirth());
        user.setPhone(updatedUser.getPhone());
        user.setGender(updatedUser.getGender());
        return userRepository.save(user);
    }

    @Override
    public void changePassword(String email, String newPassword) {
        if (!isValidPassword(newPassword)) {
            throw new IllegalArgumentException("Password must contain at least one lowercase letter, one uppercase letter, one number, and be at least 8 characters long.");
        }
        User user = findByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?!.*\\s).{8,}$");
    }

    //    @Override
//    public Page<User> findByRole(String role, Pageable pageable) {
//        return userRepository.findByRole(role, pageable);
//    }
//
//    @Override
//    public Page<User> findAllExceptAdminAndGuest(Pageable pageable) {
//        return userRepository.findAllExceptRoles(pageable, "ROLE_ADMIN", "ROLE_GUEST");
//    }
    @Override
    public void sendResetPasswordEmail(String email, HttpServletRequest request) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("No user found with email: " + email);
        }

        String token = UUID.randomUUID().toString();
        ResetToken resetToken = new ResetToken(token, user);
        resetTokenRepository.save(resetToken);


        String baseUrl = UrlUtil.getBaseUrl(request);

        // Tạo URL đặt lại mật khẩu
        String resetUrl = baseUrl + "/reset-password?token=" + token;

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Đặt lại mật khẩu");

            String htmlMsg = "<html>" +
                    "<body>" +
                    "<p>Xác Nhận Đặt Lại Mật Khẩu, Nhấn vào nút bên dưới:</p>" +
                    "<a href=\"" + resetUrl + "\" style=\"display: inline-block; padding: 10px 20px; font-size: 16px; color: white; background-color: #007bff; text-decoration: none; border-radius: 4px;\">Xác Nhận Đổi Mật Khẩu</a>" +
                    "</body>" +
                    "</html>";
            helper.setText(htmlMsg, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public boolean isResetTokenValid(String token) {
        ResetToken resetToken = resetTokenRepository.findByToken(token);
        return resetToken != null && !resetToken.isExpired();
    }

    @Override
    public List<User> findByRole(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public List<User> findAllExceptAdminAndGuest() {
        return userRepository.findAllExceptRoles();
    }


    @Override
    public void updatePasswordReset(String token, String password) {
        // Kiểm tra tính hợp lệ của mật khẩu
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Password must be at least 8 characters long, contain one uppercase letter, one lowercase letter, one digit, and no spaces.");
        }

        // Lấy thông tin token từ cơ sở dữ liệu
        ResetToken resetToken = resetTokenRepository.findByToken(token);
        if (resetToken == null || resetToken.isExpired()) {
            throw new IllegalArgumentException("Invalid or expired token.");
        }

        // Lấy thông tin người dùng từ token và mã hóa mật khẩu mới
        User user = resetToken.getUser();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(password));

        // Lưu thông tin người dùng đã cập nhật mật khẩu
        userRepository.save(user);

        // Xóa token sau khi đã sử dụng
        resetTokenRepository.delete(resetToken);
    }


    @Override
    public long countTotalUsers() {
        return userRepository.countTotalUsers();
    }

    @Override
    public long countActiveUsers() {
        return userRepository.countActiveUsers();
    }

    @Override
    public long countInactiveUsers() {
        return userRepository.countInactiveUsers();
    }

    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void changeUserStatus(User user) {
        if (user.isActive()) {
            user.setIsActive(false);
        } else {
            user.setIsActive(true);
        }
        userRepository.save(user);
    }

    @Override
    public void updateProfilePicture(String email, String profileImage) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        user.setProfileImage(profileImage);
        userRepository.save(user);
    }


    @Override
    public long countUsersByRole(String roleName) {
        return userRepository.countByRoleRoleName(roleName);
    }

    @Override
    public long countUsersByRoleAndStatus(String roleName, boolean isActive) {
        return userRepository.countByRoleRoleNameAndIsActive(roleName, isActive);

    }

    @Override
    public Page<User> searchExpert(String expertName, Pageable pageable){
        return userRepository.searchExpert(expertName, 3, pageable);
    }

}


package com.quiz.g4.service;

import com.quiz.g4.entity.Role;
import com.quiz.g4.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    User findUserByUserId(Integer userId);

    List<User> findByRoleId(int roleId);

    User updateUser(String email, User updatedUser);
    void changeUserStatus(User user);

    void changePassword(String email, String newPassword);

    Page<User> getAllExpert(int page, int size);
//    Page<User> findByRole(String role, Pageable pageable);
//    Page<User> findAllExceptAdminAndGuest(Pageable pageable);

    void saveUser(User user);

    boolean isValidPassword(String password);

    void sendResetPasswordEmail(String email, HttpServletRequest request);

    void updatePasswordReset(String token, String password);

    boolean isResetTokenValid(String token);

    Page<User> findByRole(String role, Pageable pageable);


    Page<User> findAllExceptAdminAndGuest(Pageable pageable);

    void updateUserStatus(Integer userId, boolean active);

//    Page<User> searchExpert(String expertName, Integer subjectId, Integer roleId, int page, int size);
    void createUser(User user);
    long countTotalUsers();
    long countActiveUsers();
    long countInactiveUsers();
//    List<Role> findRolesForUserCreation();
    void updateProfilePicture(String email, String profileImage);
    //for marketing
    Page<User> findUsersByRole(String roleName, Pageable pageable);
//    User toggleUserStatus(Integer userId);
    long countUsersByRole(String roleName);
    long countUsersByRoleAndStatus(String roleName, boolean isActive);
}


package com.quiz.g4.service;

import com.quiz.g4.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

   User findByEmail(String email);

    User findById(int userId);

    List<User> findByRoleId(int roleId);

    User updateUser(String email, User updatedUser);

    void changePassword(String email, String newPassword);

    Page<User> getAllExpert(int page, int size);

    void saveUser(User user);
}


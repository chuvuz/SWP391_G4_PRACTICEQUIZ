package com.quiz.g4.service;

import com.quiz.g4.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    List<User> findByRoleId(int roleId);
}


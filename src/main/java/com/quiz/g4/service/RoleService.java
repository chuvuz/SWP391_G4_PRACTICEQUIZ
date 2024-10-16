package com.quiz.g4.service;

import com.quiz.g4.entity.Role;
import com.quiz.g4.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
    List<Role> findRolesForUserCreation();
    List<User> findByRoleId(int roleId);
}

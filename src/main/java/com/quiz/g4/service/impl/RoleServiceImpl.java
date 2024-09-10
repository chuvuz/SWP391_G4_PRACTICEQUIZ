package com.quiz.g4.service.impl;

import com.quiz.g4.entity.User;
import com.quiz.g4.repository.UserRepository;
import com.quiz.g4.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> findByRoleId(int roleId) {
        return userRepository.findByRoleRoleId(roleId);
    }
}

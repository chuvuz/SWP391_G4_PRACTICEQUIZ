package com.quiz.g4.service.impl;

import com.quiz.g4.entity.User;
import com.quiz.g4.repository.UserRepository;
import com.quiz.g4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findByRoleId(int roleId) {
        return userRepository.findByRoleRoleId(roleId);
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
        user.setDescription(updatedUser.getDescription());
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

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?!.*\\s).{8,}$");
    }

}


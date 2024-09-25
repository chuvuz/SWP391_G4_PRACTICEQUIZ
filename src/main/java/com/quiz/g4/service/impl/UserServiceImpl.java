package com.quiz.g4.service.impl;

import com.quiz.g4.entity.Role;
import com.quiz.g4.entity.User;
import com.quiz.g4.repository.RoleRepository;
import com.quiz.g4.repository.UserRepository;
import com.quiz.g4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserByUserId(Integer userId){return userRepository.findUserByUserId(userId);}

    @Override
    public List<User> findByRoleId(int roleId) {
        return userRepository.findByRoleRoleId(roleId);
    }

    @Override
    public Page<User> getAllExpert(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findByRoleRoleId(3, pageable);
    }

    @Override
    public Page<User> searchExpert(String expertName, Integer subjectId, Integer roleId, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.searchExpert(expertName, subjectId, roleId, pageable);
    }

    @Override
    public void saveUser(User user) {


        // Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set other fields like fullName và email
        user.setFullName(user.getFullName());
        user.setEmail(user.getEmail());

        // Kiểm tra role người dùng
        Role role = roleRepository.findRoleByRoleId(1);
        if (role == null) {
            throw new IllegalArgumentException("Invalid role");
        } else {
            user.setRole(role);
        }

        // Đảm bảo subject vẫn giữ nguyên
        user.setSubject(user.getSubject());

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

    public boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?!.*\\s).{8,}$");
    }

}


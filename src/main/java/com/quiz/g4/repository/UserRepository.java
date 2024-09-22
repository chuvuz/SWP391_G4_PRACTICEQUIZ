package com.quiz.g4.repository;


import com.quiz.g4.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, CrudRepository<User, Integer> {
    User findByEmail(String email);

    User findUserByUserId(Integer userId);

    List<User> findByRoleRoleId(int roleId);

    Page<User> findByRoleRoleId(int roleId, Pageable pageable);

}


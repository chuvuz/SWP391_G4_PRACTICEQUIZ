package com.quiz.g4.repository;


import com.quiz.g4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, CrudRepository<User, Integer> {
    User findByEmail(String email);
}


package com.quiz.g4.repository;


import com.quiz.g4.entity.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {

     ResetToken findByToken(String token);
}

package com.quiz.g4.repository;

import com.quiz.g4.entity.QuestionBank;
<<<<<<< Updated upstream
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionBankRepository extends JpaRepository<QuestionBank, Integer> {
=======
import com.quiz.g4.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionBankRepository extends JpaRepository<QuestionBank,Integer> {
>>>>>>> Stashed changes
}

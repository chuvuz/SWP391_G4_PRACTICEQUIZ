package com.quiz.g4.repository;

import com.quiz.g4.entity.AnswerOption;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Integer> {
}

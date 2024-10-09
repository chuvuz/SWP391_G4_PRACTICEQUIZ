package com.quiz.g4.repository;

import com.quiz.g4.entity.LessonResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonResultRepository extends JpaRepository<LessonResult,Integer> {

}
package com.quiz.g4.repository;

import com.quiz.g4.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject,Integer> {

    Subject findById(int id);

}

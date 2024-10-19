package com.quiz.g4.repository;

import com.quiz.g4.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject,Integer> {


    Optional<Subject> findBySubjectName(String subjectName);


    @Query("SELECT s FROM Subject s WHERE s.isActive = true")
    Page<Subject> findAllActiveSubject(Pageable pageable);
}

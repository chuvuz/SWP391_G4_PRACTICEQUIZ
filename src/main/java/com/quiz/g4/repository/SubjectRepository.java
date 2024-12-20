package com.quiz.g4.repository;

import com.quiz.g4.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {


    Subject findBySubjectName(String subjectName);


    @Query("SELECT s FROM Subject s WHERE s.isActive = true")
    Page<Subject> findAllActiveSubject(Pageable pageable);


    @Query("SELECT s FROM Subject s WHERE s.subjectName LIKE %:subjectName%")
    Page<Subject> searchSubjectAll(@Param("subjectName") String subjectName, Pageable pageable);

    Subject findBySubjectId(Integer subjectId);

    @Query("SELECT s FROM Subject s WHERE (:subjectName IS NULL OR s.subjectName LIKE CONCAT('%', :subjectName, '%'))" +
            " AND (:categoryId IS NULL OR s.category.categoryId = :categoryId)" +
            " AND s.isActive = true")
    Page<Subject> searchSubject(@Param("subjectName") String subjectName,
                                @Param("categoryId") Integer categoryId,
                                Pageable pageable);

    boolean existsBySubjectName(String subjectName);

    @Query("SELECT s FROM Subject s JOIN FETCH s.lessons l WHERE s.subjectId = :subjectId ORDER BY l.createdDate ASC")
    Subject getSubjectByIdWithLessonAsc(@Param("subjectId") Integer subjectId);


    Page<Subject> findByCategory_CategoryId(Integer categoryId, Pageable pageable);


    @Query("SELECT s FROM Subject s WHERE (:subjectName IS NULL OR s.subjectName LIKE CONCAT('%', :subjectName, '%'))" +
            " AND (:categoryId IS NULL OR s.category.categoryId = :categoryId)")
    Page<Subject> searchSubjectAll(String subjectName, Integer categoryId, Pageable pageable);
}

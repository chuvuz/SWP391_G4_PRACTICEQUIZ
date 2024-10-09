package com.quiz.g4.repository;

import com.quiz.g4.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    // Phương thức để lấy tất cả phản hồi theo blogId
    List<Feedback> findByBlogId(Integer blogId);
    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.blogId = :blogId")
    long countFeedbackByBlogId(@Param("blogId") Integer blogId);
}
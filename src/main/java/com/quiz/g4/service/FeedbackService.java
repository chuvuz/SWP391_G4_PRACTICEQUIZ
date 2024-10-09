package com.quiz.g4.service;

import com.quiz.g4.entity.Feedback;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FeedbackService {
    List<Feedback> getFeedbackByBlogId(Integer blogId);

    void saveFeedback(Feedback feedback);

    long getFeedbackCountByBlogId(Integer blogId);
}
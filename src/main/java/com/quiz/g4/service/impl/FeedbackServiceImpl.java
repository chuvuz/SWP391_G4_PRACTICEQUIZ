package com.quiz.g4.service.impl;

import com.quiz.g4.entity.Feedback;
import com.quiz.g4.repository.FeedbackRepository;
import com.quiz.g4.repository.QuizRepository;
import com.quiz.g4.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Override
    public List<Feedback> getFeedbackByBlogId(Integer blogId) {
        return feedbackRepository.findByBlogId(blogId);
    }

    @Override
    public void saveFeedback(Feedback feedback) {
        feedbackRepository.save(feedback);
    }


}
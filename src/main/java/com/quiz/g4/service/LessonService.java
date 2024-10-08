package com.quiz.g4.service;

import com.quiz.g4.entity.Lesson;
import org.springframework.stereotype.Service;

@Service
public interface LessonService {
    Lesson getLessonWithQuestions(Integer lessonId);
}

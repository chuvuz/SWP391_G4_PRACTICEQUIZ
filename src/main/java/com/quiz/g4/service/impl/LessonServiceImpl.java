package com.quiz.g4.service.impl;

import com.quiz.g4.entity.Lesson;

import com.quiz.g4.repository.LessonRepository;
import com.quiz.g4.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonServiceImpl implements LessonService {


    @Autowired
    private LessonRepository lessonRepository;
    @Override
    public Lesson getLessonWithQuestions(Integer lessonId) {
        return lessonRepository.findLessonWithQuestionsById(lessonId);
    }

    @Override
    public Lesson getLessonById(Object lessonId) {
        // Kiểm tra và chuyển đổi lessonId về kiểu Integer nếu cần thiết
        if (lessonId instanceof Integer) {
            return lessonRepository.findById((Integer) lessonId)
                    .orElse(null); // Trả về bài học hoặc null nếu không tìm thấy
        }
        return null;
    }

}

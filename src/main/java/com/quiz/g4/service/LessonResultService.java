package com.quiz.g4.service;

import com.quiz.g4.entity.LessonResult;
import org.springframework.stereotype.Service;

@Service
public interface LessonResultService {
    void saveLessonResult(LessonResult lessonResult);

    LessonResult findLessonResultById(Integer resultId);
}

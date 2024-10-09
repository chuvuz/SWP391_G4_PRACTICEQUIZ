package com.quiz.g4.service.impl;

import com.quiz.g4.entity.LessonResult;
import com.quiz.g4.repository.LessonRepository;
import com.quiz.g4.repository.LessonResultRepository;
import com.quiz.g4.service.LessonResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonResultServiceImpl implements LessonResultService {

    @Autowired
    private LessonResultRepository lessonResultRepository;

    @Override
    public void saveLessonResult(LessonResult lessonResult) {
        lessonResultRepository.save(lessonResult);
    }

    @Override
    public LessonResult findLessonResultById(Integer resultId) {
        return lessonResultRepository.findById(resultId)
                .orElse(null);  // Return null if not found
    }
}

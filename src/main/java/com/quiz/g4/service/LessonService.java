package com.quiz.g4.service;

import com.quiz.g4.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LessonService {
//    Lesson getLessonWithQuestions(Integer lessonId);
//
//    Lesson getLessonById(Object lessonId);
//
//    List<Lesson> getAllLessons();

    Page<Lesson> getLessons(Pageable pageable);

    List<Lesson> getAllLessons();

    Lesson getLessonById(Integer lessonId);

   List<Lesson> getLessonsBySubjectId(Integer subjectId);
}

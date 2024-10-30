package com.quiz.g4.service.impl;

import com.quiz.g4.entity.Lesson;

import com.quiz.g4.repository.LessonRepository;
import com.quiz.g4.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {


    @Autowired
    private LessonRepository lessonRepository;
//    @Override
//    public Lesson getLessonWithQuestions(Integer lessonId) {
//        return lessonRepository.findLessonWithQuestionsById(lessonId);
//    }

//    @Override
//    public Lesson getLessonById(Object lessonId) {
//        // Kiểm tra và chuyển đổi lessonId về kiểu Integer nếu cần thiết
//        if (lessonId instanceof Integer) {
//            return lessonRepository.findById((Integer) lessonId)
//                    .orElse(null); // Trả về bài học hoặc null nếu không tìm thấy
//        }
//        return null;
//    }
//
//    @Override
//    public List<Lesson> getAllLessons() {
//        return lessonRepository.findAll();
//
//    }

    @Override
    public Page<Lesson> getLessons(Pageable pageable) {
        return lessonRepository.findAllByDesc(pageable);
    }

    @Override
    public Lesson getLessonById(Integer lessonId) {
        return lessonRepository.findById(lessonId).orElseThrow(null);
    }

    @Override
    public List<Lesson> getLessonsBySubjectId(Integer subjectId) {
        return lessonRepository.findBySubject_SubjectId(subjectId);
    }

    @Override
    public List<Lesson> getAllLessons() {return lessonRepository.findAll();}

}

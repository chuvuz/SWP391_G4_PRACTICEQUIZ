package com.quiz.g4.controller.questionBank;

import com.quiz.g4.entity.Lesson;
import com.quiz.g4.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lessonsBySubject")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @GetMapping("/{subjectId}")
    public ResponseEntity<List<Lesson>> getLessonsBySubject(@PathVariable Integer subjectId) {
        List<Lesson> lessons = lessonService.getLessonsBySubjectId(subjectId);
        return ResponseEntity.ok(lessons); // Trả về danh sách lesson dưới dạng JSON
    }
}


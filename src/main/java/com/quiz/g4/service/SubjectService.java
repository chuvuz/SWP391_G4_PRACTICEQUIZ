package com.quiz.g4.service;

import com.quiz.g4.entity.Subject;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface SubjectService {
    List<Subject> getAllSubjects();

    Subject findById(int id);

}

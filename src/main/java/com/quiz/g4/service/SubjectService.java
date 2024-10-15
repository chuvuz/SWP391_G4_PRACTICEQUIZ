package com.quiz.g4.service;

import com.quiz.g4.entity.Subject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SubjectService {
    List<Subject> getAllSubjects();

    Subject findById(int id);


    void createSubject(String subjectName, boolean isActive);


    //void updateSubject(String subjectName);

    void updateSubject(int subjectId, String subjectName, boolean isActive);
}
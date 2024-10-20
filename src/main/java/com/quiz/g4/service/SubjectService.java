package com.quiz.g4.service;

import com.quiz.g4.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SubjectService {
    List<Subject> getAllSubjects();

    Subject findById(int id);


    void createSubject(String subjectName, boolean isActive);


    //void updateSubject(String subjectName);

    void updateSubject(int subjectId, String subjectName, boolean isActive);

    Page<Subject> getAllSubject(int page, int size);

    Page<Subject> searchSubject(String subjectName, int page, int size);

    Subject getSubjectById(Integer subjectId);
}
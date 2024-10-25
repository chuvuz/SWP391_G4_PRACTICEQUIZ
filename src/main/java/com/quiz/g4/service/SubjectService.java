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

    void updateSubjectWithImage(int id, String subjectName, boolean isActive, MultipartFile imageFile);

    void createSubjectWithImage(String subjectName, boolean isActive, MultipartFile imageFile);

    // Tạo môn học với chuỗi URL ảnh
    void createSubjectWithImageUrl(String subjectName, boolean isActive, String imageUrl);

    // Cập nhật môn học với chuỗi URL ảnh
    void updateSubjectWithImageUrl(int id, String subjectName, boolean isActive, String imageUrl);

    Page<Subject> getAllSubjectNoCondition(int page, int size);

    Page<Subject> searchSubjectAll(String subjectName, int page, int size);
}
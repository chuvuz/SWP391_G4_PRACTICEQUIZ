package com.quiz.g4.service.impl;

import com.quiz.g4.entity.Subject;
import com.quiz.g4.repository.SubjectRepository;
import com.quiz.g4.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @Override
    public Subject findById(int id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
    }

    @Override
    public void createSubject(String subjectName, boolean isActive) {
        Subject newSubject = Subject.builder()
                .subjectName(subjectName)
                .isActive(isActive)
                .build();
        subjectRepository.save(newSubject);
    }

    @Override
    public void updateSubject(int subjectId, String subjectName, boolean isActive) {
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalSubject.isPresent()) {
            Subject subject = optionalSubject.get();
            subject.setSubjectName(subjectName);
            subject.setIsActive(isActive);
            subjectRepository.save(subject);
        } else {
            throw new NoSuchElementException("Subject with ID " + subjectId + " not found.");
        }
    }

    @Override
    public Page<Subject> getAllSubject(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return subjectRepository.findAllActiveSubject(pageable);
    }

    @Override
    public Page<Subject> searchSubject(String subjectName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return subjectRepository.searchSubject(subjectName, pageable);
    }

    @Override
    public Subject getSubjectById(Integer subjectId) {
        return subjectRepository.findBySubjectId(subjectId);
    }

    @Override
    public void updateSubjectWithImage(int id, String subjectName, boolean isActive, MultipartFile imageFile) {

    }

    @Override
    public void createSubjectWithImage(String subjectName, boolean isActive, MultipartFile imageFile) {

    }

    // Tạo môn học với chuỗi URL ảnh
    @Override
    public void createSubjectWithImageUrl(String subjectName, boolean isActive, String imageUrl) {
        // Xử lý khi người dùng nhập URL ảnh
        Subject newSubject = Subject.builder()
                .subjectName(subjectName)
                .isActive(isActive)
                .subjectImage(imageUrl)  // Lưu URL ảnh thay vì file ảnh
                .build();
        subjectRepository.save(newSubject);
    }

    // Cập nhật môn học với chuỗi URL ảnh
    @Override
    public void updateSubjectWithImageUrl(int id, String subjectName, boolean isActive, String imageUrl) {
        Optional<Subject> optionalSubject = subjectRepository.findById(id);

        if (optionalSubject.isPresent()) {
            Subject subject = optionalSubject.get();
            subject.setSubjectName(subjectName);
            subject.setIsActive(isActive);

            // Nếu người dùng nhập URL ảnh mới, cập nhật URL ảnh
            if (imageUrl != null && !imageUrl.isEmpty()) {
                subject.setSubjectImage(imageUrl);  // Cập nhật URL ảnh
            }

            subjectRepository.save(subject);
        } else {
            throw new NoSuchElementException("Subject with ID " + id + " not found.");
        }
    }

    @Override
    public Page<Subject> getAllSubjectNoCondition(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return subjectRepository.findAll(pageable);
    }
}

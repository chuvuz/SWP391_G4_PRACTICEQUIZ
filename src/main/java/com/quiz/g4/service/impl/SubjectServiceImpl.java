package com.quiz.g4.service.impl;

import com.quiz.g4.entity.Subject;
import com.quiz.g4.repository.SubjectRepository;
import com.quiz.g4.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

  // Đường dẫn thư mục nơi lưu trữ file upload
    //private String uploadDir="user-images";

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

//    @Override
//    public void updateSubject(String subjectName) {
//
//    }

//    @Override
//    public void updateSubject(String subjectName) {
//        Optional<Subject> optionalSubject = subjectRepository.findBySubjectName(subjectName);
//
//        if (optionalSubject.isPresent()) {
//            Subject subject = optionalSubject.get();
//            subject.setSubjectName(subjectName);
//            subjectRepository.save(subject);
//        } else {
//            throw new NoSuchElementException("Subject with name " + subjectName + " not found.");
//        }
//    }


    @Override
    public void updateSubject(int subjectId, String subjectName, boolean isActive) {
        // Tìm subject theo ID
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalSubject.isPresent()) {
            Subject subject = optionalSubject.get();

            // Cập nhật thông tin subject
            subject.setSubjectName(subjectName);
            subject.setActive(isActive);

            // Lưu subject đã cập nhật vào cơ sở dữ liệu
            subjectRepository.save(subject);
        } else {
            // Có thể ném ra ngoại lệ nếu subject không tồn tại
            throw new NoSuchElementException("Subject with ID " + subjectId + " not found.");
        }
    }



}

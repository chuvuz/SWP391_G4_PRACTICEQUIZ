package com.quiz.g4.service;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface SubjectService {
    List<Subject> getAllSubjects();
}

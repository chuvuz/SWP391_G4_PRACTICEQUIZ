package com.quiz.g4.service;

import com.quiz.g4.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {


    List<Category> getAllCategory();
}

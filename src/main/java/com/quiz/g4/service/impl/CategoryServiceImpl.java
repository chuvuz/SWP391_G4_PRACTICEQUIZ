package com.quiz.g4.service.impl;

import com.quiz.g4.entity.Category;
import com.quiz.g4.repository.CategoryRepository;
import com.quiz.g4.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }
}

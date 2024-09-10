package com.quiz.g4.service.impl;

import com.quiz.g4.entity.Blog;
import com.quiz.g4.repository.BlogRepository;
import com.quiz.g4.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public List<Blog> findAllBlogs() {
        return blogRepository.findAll();
    }
}

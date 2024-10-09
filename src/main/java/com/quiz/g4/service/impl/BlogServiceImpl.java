package com.quiz.g4.service.impl;

import com.quiz.g4.entity.Blog;
import com.quiz.g4.repository.BlogRepository;
import com.quiz.g4.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public List<Blog> findAllBlogs() {
        return blogRepository.findAll();
    }

    @Override
    public List<Blog> getAllBlogs() {
        return blogRepository.findAllByOrderByCreatedDateDesc();
    }

    @Override
    public Optional<Blog> getBlogById(Integer blogId) {
        return blogRepository.findById(blogId);

    }

    @Override
    public Page<Blog> getAllBlogsPage(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }
}
package com.quiz.g4.service;

import com.quiz.g4.entity.Blog;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BlogService {

    List<Blog> findAllBlogs();
}

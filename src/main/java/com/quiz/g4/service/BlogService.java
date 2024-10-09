package com.quiz.g4.service;

import com.quiz.g4.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BlogService {

    List<Blog> findAllBlogs();

    List<Blog> getAllBlogs();

    Optional<Blog> getBlogById(Integer blogId);

    Page<Blog> getAllBlogsPage(Pageable pageable);
}
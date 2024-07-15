package com.example.example.service;

import com.example.example.entities.Comment;
import com.example.example.model.CommentFilter;

import java.util.List;

public interface CommentService
{
    List<Comment> findAllByNewsId(CommentFilter filter);

    Comment findById(Long id);

    Comment save(Comment comment);

    Comment update(Comment comment);

    void deleteById(Long id, Comment comment);
}

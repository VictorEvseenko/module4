package com.example.example.mapper;

import com.example.example.entities.Comment;
import com.example.example.entities.News;
import com.example.example.model.DeletingCommentRequest;
import com.example.example.model.DeletingNewsRequest;
import com.example.example.model.UpsertCommentRequest;
import com.example.example.service.NewsServiceImpl;
import com.example.example.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CommentMapperDelegate implements CommentMapper
{
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private NewsServiceImpl newsService;

    @Override
    public Comment requestToComment(UpsertCommentRequest request) {
        Comment comment = new Comment();
        comment.setText(request.getText());
        comment.setNews(newsService.findById(request.getNewsId()));
        comment.setUser(userService.findById(request.getUserId()));
        return comment;
    }

    @Override
    public Comment requestToComment(Long commentId, UpsertCommentRequest request) {
        Comment comment = requestToComment(request);
        comment.setId(commentId);
        return comment;
    }

    @Override
    public Comment requestToCommentForDeleting(Long id, DeletingCommentRequest request) {
        Comment comment = new Comment();
        comment.setUser(userService.findById(request.getUserId()));
        comment.setId(id);
        return comment;
    }

}

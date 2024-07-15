package com.example.example.service;

import com.example.example.BeanUtils;
import com.example.example.aop.CheckingComment;
import com.example.example.entities.Comment;
import com.example.example.entities.News;
import com.example.example.entities.User;
import com.example.example.exception.EntityNotFoundException;
import com.example.example.model.CommentFilter;
import com.example.example.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService
{
    private final CommentRepository commentRepository;
    private final UserServiceImpl userService;
    private final NewsServiceImpl newsService;

    @Override
    public List<Comment> findAllByNewsId(CommentFilter filter) {
        return commentRepository.findAllByNewsId(filter.getNewsId());
    }

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Комментарий с ID {0} не найден", id)));
    }

    @Override
    public Comment save(Comment comment) {
        User user = userService.findById(comment.getUser().getId());
        News news = newsService.findById(comment.getNews().getId());
        comment.setUser(user);
        comment.setNews(news);
        return commentRepository.save(comment);
    }

    @CheckingComment
    @Override
    public Comment update(Comment comment) {
        User user = userService.findById(comment.getUser().getId());
        News news = newsService.findById(comment.getNews().getId());
        Comment existedComment = findById(comment.getId());
        BeanUtils.copyNonNullProperties(comment, existedComment);
        existedComment.setUser(user);
        existedComment.setNews(news);
        return commentRepository.save(existedComment);
    }

    @CheckingComment
    @Override
    public void deleteById(Long id, Comment comment) {
        commentRepository.deleteById(id);
    }
}

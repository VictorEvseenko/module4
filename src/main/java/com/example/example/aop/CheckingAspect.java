package com.example.example.aop;

import com.example.example.entities.News;
import com.example.example.entities.Comment;
import com.example.example.exception.AccessDeniedException;
import com.example.example.service.CommentServiceImpl;
import com.example.example.service.NewsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Aspect
@Service
@RequiredArgsConstructor
public class CheckingAspect
{
    private final NewsServiceImpl newsService;
    private final CommentServiceImpl commentService;

    @Before("@annotation(CheckingMessage)")
    public void checkNewsBefore(JoinPoint jp) {
        Object[] arguments = jp.getArgs();
        for (Object arg : arguments) {
            if (arg instanceof News news) {
                if (!newsService.findById(news.getId()).getUser().getId().equals(news.getUser().getId())) {
                    throw new AccessDeniedException("Отказано в доступе!");
                }
            }
        }
    }

    @Before("@annotation(CheckingComment)")
    public void checkCommentBefore(JoinPoint jp) {
        Object[] arguments = jp.getArgs();
        for (Object arg : arguments) {
            if (arg instanceof Comment comment) {
                if (!commentService.findById(comment.getId()).getUser().getId().equals(comment.getUser().getId())) {
                    throw new AccessDeniedException("Отказано в доступе!");
                }
            }
        }
    }

}
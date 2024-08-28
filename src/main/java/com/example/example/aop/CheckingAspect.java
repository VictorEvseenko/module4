package com.example.example.aop;

import com.example.example.entities.*;
import com.example.example.exception.AccessDeniedException;
import com.example.example.exception.EntityNotFoundException;
import com.example.example.repository.UserRepository;
import com.example.example.service.CommentServiceImpl;
import com.example.example.service.NewsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Aspect
@Service
@RequiredArgsConstructor
public class CheckingAspect
{
    private final NewsServiceImpl newsService;
    private final CommentServiceImpl commentService;
    private final UserRepository userRepository;

    @Before("@annotation(CheckingUserForFindingOrDeleting)")
    public void checkUserForFindingOrDeleting(JoinPoint jp)
    {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Object[] arguments = jp.getArgs();
        long observableId = 0L;

        for (Object arg : arguments) {
            if (arg instanceof Long) {
                observableId = Long.parseLong(arg.toString());
            }
        }

        long finalObservableId = observableId;
        User observableUser = userRepository.findById(observableId).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Пользователь с ID {0} не найден", finalObservableId)));

        User authorizatingUser = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Пользователь с именем {0} не найден", userDetails.getUsername())));;

        if (!observableUser.equals(authorizatingUser)) {
            if (!adminOrModeratorVerification(authorizatingUser)) {
                throw new AccessDeniedException("Отказано в доступе!");
            }
        }
    }

    @Before("@annotation(CheckingUserForUpdating)")
    public void checkUserForUpdating(JoinPoint jp)
    {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Object[] arguments = jp.getArgs();
        User observableUser = new User();

        for (Object arg : arguments) {
            if (arg instanceof User user) {
                observableUser = userRepository.findById(user.getId()).orElseThrow(() ->
                        new EntityNotFoundException(MessageFormat.format("Пользователь с ID {0} не найден", user.getId())));
            }
        }

        User authorizatingUser = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Пользователь с именем {0} не найден", userDetails.getUsername())));;

        if (!observableUser.equals(authorizatingUser)) {
            if (!adminOrModeratorVerification(authorizatingUser)) {
                throw new AccessDeniedException("Отказано в доступе!");
            }
        }
    }

    @Before("@annotation(CheckingNewsForUpdating)")
    public void checkNewsForUpdating(JoinPoint jp)
    {
        Object[] arguments = jp.getArgs();
        for (Object arg : arguments) {
            if (arg instanceof News news) {
                if (!newsService.findById(news.getId()).getUser().getId().equals(news.getUser().getId())) {
                    throw new AccessDeniedException("Отказано в доступе!");
                }
            }
        }
    }

    @Before("@annotation(CheckingNewsForDeleting)")
    public void checkNewsForDeleting(JoinPoint jp)
    {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Object[] arguments = jp.getArgs();
        long newsId = 0L;

        for (Object arg : arguments) {
            if (arg instanceof Long) {
                newsId = Long.parseLong(arg.toString());
            }
        }

        long finalNewsId = newsId;
        User observableUser = userRepository.findById(newsService.findById(newsId).getUser().getId()).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Пользователь с ID {0} не найден", newsService.findById(finalNewsId).getUser().getId())));

        User authorizatingUser = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Пользователь с именем {0} не найден", userDetails.getUsername())));;

        if (!observableUser.equals(authorizatingUser)) {
            if (!adminOrModeratorVerification(authorizatingUser)) {
                throw new AccessDeniedException("Отказано в доступе!");
            }
        }
    }

    @Before("@annotation(CheckingCommentForUpdating)")
    public void checkCommentForUpdating(JoinPoint jp) {
        Object[] arguments = jp.getArgs();
        for (Object arg : arguments) {
            if (arg instanceof Comment comment) {
                if (!commentService.findById(comment.getId()).getUser().getId().equals(comment.getUser().getId())) {
                    throw new AccessDeniedException("Отказано в доступе!");
                }
            }
        }
    }

    @Before("@annotation(CheckingCommentForDeleting)")
    public void checkCommentForDeleting(JoinPoint jp)
    {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Object[] arguments = jp.getArgs();
        long commentId = 0L;

        for (Object arg : arguments) {
            if (arg instanceof Long) {
                commentId = Long.parseLong(arg.toString());
            }
        }

        long finalCommentId = commentId;
        User observableUser = userRepository.findById(commentService.findById(commentId).getUser().getId()).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Пользователь с ID {0} не найден", commentService.findById(finalCommentId).getUser().getId())));

        User authorizatingUser = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Пользователь с именем {0} не найден", userDetails.getUsername())));;

        if (!observableUser.equals(authorizatingUser)) {
            if (!adminOrModeratorVerification(authorizatingUser)) {
                throw new AccessDeniedException("Отказано в доступе!");
            }
        }
    }

    private boolean adminOrModeratorVerification(User user) {
        boolean checking = false;
        for (Role role : user.getRoles()) {
            if (role.getAuthority().equals(RoleType.ROLE_ADMIN) || role.getAuthority().equals(RoleType.ROLE_MODERATOR)) {
                checking = true;
                break;
            }
        }
        return checking;
    }

}
package com.example.example.mapper;

import com.example.example.entities.News;
import com.example.example.model.*;
import com.example.example.service.CategoryServiceImpl;
import com.example.example.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class NewsMapperDelegate implements NewsMapper
{
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @Override
    public News requestToNews(UpsertNewsRequest request) {
        News news = new News();
        news.setTitle(request.getTitle());
        news.setText(request.getText());
        news.setUser(userService.findById(request.getUserId()));
        news.setCategory(categoryService.findById(request.getCategoryId()));
        return news;
    }

    @Override
    public News requestToNews(Long id, UpsertNewsRequest request) {
        News news = requestToNews(request);
        news.setId(id);
        return news;
    }

    @Override
    public NewsResponseWithCountComments newsToResponseWithCounts(News news) {
        NewsResponseWithCountComments newsResponseWithCountComments = new NewsResponseWithCountComments();
        newsResponseWithCountComments.setId(news.getId());
        newsResponseWithCountComments.setText(news.getText());
        newsResponseWithCountComments.setTitle(news.getTitle());
        newsResponseWithCountComments.setCountComments(news.getComments().size());
        return newsResponseWithCountComments;
    }

    @Override
    public List<NewsResponseWithCountComments> newsListToResponseListWithCountComments(List<News> newsList) {
        return newsList.stream().map(this::newsToResponseWithCounts).collect(Collectors.toList());
    }
}
package com.example.example.service;

import com.example.example.entities.News;
import com.example.example.model.NewsFilter;
import com.example.example.model.Pagination;

import java.util.List;

public interface NewsService
{
    List<News> findAllWithFilter(NewsFilter filter);

    List<News> findAll(Pagination page);

    News findById(Long id);

    News save(News news);

    News update(News news);

    void deleteById(Long id, News news);
}

package com.example.example.service;

import com.example.example.aop.CheckingNewsForDeleting;
import com.example.example.aop.CheckingNewsForUpdating;
import com.example.example.entities.Category;
import com.example.example.exception.EntityNotFoundException;
import com.example.example.entities.User;
import com.example.example.entities.News;
import com.example.example.model.NewsFilter;
import com.example.example.model.Pagination;
import com.example.example.repository.NewsRepository;
import com.example.example.BeanUtils;
import com.example.example.repository.NewsSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService
{
    private final NewsRepository newsRepository;
    private final UserServiceImpl userService;
    private final CategoryServiceImpl categoryService;

    @Override
    public List<News> findAllWithFilter(NewsFilter filter) {
        return newsRepository.findAll(NewsSpecification.withFilter(filter));
    }

    @Override
    public List<News> findAll(Pagination page) {
        return newsRepository.findAll(PageRequest.of(page.getPageNumber(), page.getPageSize())).getContent();
    }

    @Override
    public News findById(Long id) {
        return newsRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Новость с ID {0} не найдена", id)));
    }

    @Override
    public News save(News news) {
        User user = userService.findById(news.getUser().getId());
        Category category = categoryService.findById(news.getCategory().getId());
        news.setUser(user);
        news.setCategory(category);
        return newsRepository.save(news);
    }

    @Override
    @CheckingNewsForUpdating
    public News update(News news)
    {
        User user = userService.findById(news.getUser().getId());
        Category category = categoryService.findById(news.getCategory().getId());
        News existedNews = findById(news.getId());
        BeanUtils.copyNonNullProperties(news, existedNews);
        existedNews.setUser(user);
        existedNews.setCategory(category);
        return newsRepository.save(existedNews);
    }

    @CheckingNewsForDeleting
    @Override
    public void deleteById(Long id)
    {
        newsRepository.deleteById(id);
    }
}
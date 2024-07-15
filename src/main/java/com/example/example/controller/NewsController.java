package com.example.example.controller;

import com.example.example.exception.AccessDeniedException;
import com.example.example.mapper.NewsMapper;
import com.example.example.entities.News;
import com.example.example.model.*;
import com.example.example.service.NewsServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController
{
    private final NewsServiceImpl newsService;
    private final NewsMapper newsMapper;

    @GetMapping("/user")
    public ResponseEntity<NewsListResponseWithCountComments> findAllByUserId(@RequestParam Long userId) {
        NewsFilter filter = new NewsFilter();
        filter.setUserId(userId);
        return ResponseEntity.ok(newsMapper.newsListToNewsListResponseWithCountComments(newsService.findAllByUserId(filter)));
    }

    @GetMapping("/category")
    public ResponseEntity<NewsListResponseWithCountComments> findAllByCategoryId(@RequestParam Long categoryId) {
        NewsFilter filter = new NewsFilter();
        filter.setCategoryId(categoryId);
        return ResponseEntity.ok(newsMapper.newsListToNewsListResponseWithCountComments(newsService.findAllByCategoryId(filter)));
    }

    @GetMapping
    public ResponseEntity<NewsListResponseWithCountComments> findAll(Pagination page) {
        return ResponseEntity.ok(newsMapper.newsListToNewsListResponseWithCountComments(newsService.findAll(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(newsMapper.newsToResponse(newsService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<NewsResponse> create(@RequestBody @Valid UpsertNewsRequest request) {
        News news = newsService.save(newsMapper.requestToNews(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(newsMapper.newsToResponse(news));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsResponse> update(@PathVariable("id") Long newsId, @RequestBody UpsertNewsRequest request) {
        try {
            News updatedNews = newsService.update(newsMapper.requestToNews(newsId, request));
            return ResponseEntity.ok(newsMapper.newsToResponse(updatedNews));
        } catch (AccessDeniedException ex) {
            ErrorResponse err = new ErrorResponse("Отказано в доступе!");
            return new ResponseEntity(err, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestBody DeletingNewsRequest request) {
        try {
            newsService.deleteById(id, newsMapper.requestToNewsForDeleting(id, request));
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException ex) {
            ErrorResponse err = new ErrorResponse("Отказано в доступе!");
            return new ResponseEntity(err, HttpStatus.BAD_REQUEST);
        }
    }
}
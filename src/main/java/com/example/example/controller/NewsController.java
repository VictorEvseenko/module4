package com.example.example.controller;

import com.example.example.exception.AccessDeniedException;
import com.example.example.mapper.NewsMapper;
import com.example.example.entities.News;
import com.example.example.model.*;
import com.example.example.service.NewsServiceImpl;
import com.example.example.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController
{
    private final NewsServiceImpl newsService;
    private final NewsMapper newsMapper;
    private final UserServiceImpl userService;

    @GetMapping("/filter")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_USER')")
    public ResponseEntity<NewsListResponseWithCountComments> findAllWithFilter(NewsFilter filter) {
        return ResponseEntity.ok(newsMapper.newsListToNewsListResponseWithCountComments(newsService.findAllWithFilter(filter)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_USER')")
    public ResponseEntity<NewsListResponseWithCountComments> findAll(Pagination page) {
        return ResponseEntity.ok(newsMapper.newsListToNewsListResponseWithCountComments(newsService.findAll(page)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_USER')")
    public ResponseEntity<NewsResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(newsMapper.newsToResponse(newsService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_USER')")
    public ResponseEntity<NewsResponse> create(@RequestBody @Valid UpsertNewsRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        request.setUserId(userService.findByUsername(userDetails.getUsername()).getId());
        News news = newsService.save(newsMapper.requestToNews(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(newsMapper.newsToResponse(news));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsResponse> update(@PathVariable("id") Long newsId, @RequestBody UpsertNewsRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            request.setUserId(userService.findByUsername(userDetails.getUsername()).getId());
            News updatedNews = newsService.update(newsMapper.requestToNews(newsId, request));
            return ResponseEntity.ok(newsMapper.newsToResponse(updatedNews));
        } catch (AccessDeniedException ex) {
            ErrorResponse err = new ErrorResponse("Отказано в доступе!");
            return new ResponseEntity(err, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            newsService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException ex) {
            ErrorResponse err = new ErrorResponse("Отказано в доступе!");
            return new ResponseEntity(err, HttpStatus.BAD_REQUEST);
        }
    }
}
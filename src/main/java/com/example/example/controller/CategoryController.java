package com.example.example.controller;

import com.example.example.entities.Category;
import com.example.example.mapper.CategoryMapper;
import com.example.example.model.*;
import com.example.example.service.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController
{
    private final CategoryServiceImpl categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_USER')")
    public ResponseEntity<CategoryListResponse> findAll(Pagination page) {
        return ResponseEntity.ok(categoryMapper.categoryListToCategoryResponseList(categoryService.findAll(page)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_USER')")
    public ResponseEntity<CategoryResponse> findById(@PathVariable long id) {
        return ResponseEntity.ok(categoryMapper.categoryToResponse(categoryService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    public ResponseEntity<CategoryResponse> create(@RequestBody UpsertCategoryRequest request) {
        Category newCategory = categoryService.save(categoryMapper.requestToCategory(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.categoryToResponse(newCategory));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    public ResponseEntity<CategoryResponse> update(@PathVariable("id") Long categoryId, @RequestBody UpsertCategoryRequest request) {
        Category updatedCategory = categoryService.update(categoryMapper.requestToCategory(categoryId, request));
        return ResponseEntity.ok(categoryMapper.categoryToResponse(updatedCategory));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

package com.example.example.service;

import com.example.example.entities.Category;
import com.example.example.model.Pagination;

import java.util.List;

public interface CategoryService
{
    List<Category> findAll(Pagination page);

    Category findById(Long id);

    Category save(Category category);

    Category update(Category category);

    void deleteById(Long id);
}

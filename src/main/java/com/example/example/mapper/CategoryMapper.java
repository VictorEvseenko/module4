package com.example.example.mapper;

import com.example.example.entities.Category;
import com.example.example.entities.User;
import com.example.example.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {NewsMapper.class})
public interface CategoryMapper
{
    Category requestToCategory(UpsertCategoryRequest request);

    @Mapping(source = "categoryId", target = "id")
    Category requestToCategory(Long categoryId, UpsertCategoryRequest request);

    CategoryResponse categoryToResponse(Category category);

    default CategoryListResponse categoryListToCategoryResponseList(List<Category> categories) {
        CategoryListResponse response = new CategoryListResponse();
        response.setCategories(categories.stream().map(this::categoryToResponse).collect(Collectors.toList()));
        return response;
    }
}

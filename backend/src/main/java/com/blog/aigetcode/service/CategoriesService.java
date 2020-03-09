package com.blog.aigetcode.service;

import com.blog.aigetcode.DTO.CategoryDto;
import com.blog.aigetcode.entity.Category;

import java.util.List;
import java.util.Set;

public interface CategoriesService {

    List<CategoryDto> getAll();
    Set<CategoryDto> getTop5();
    Category create(CategoryDto category);
    Category update(CategoryDto category);
    boolean delete(Long id);

}

package com.blog.aigetcode.service.impl;

import com.blog.aigetcode.DTO.CategoryDto;
import com.blog.aigetcode.entity.Category;
import com.blog.aigetcode.repositories.CategoryRepository;
import com.blog.aigetcode.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoriesServiceImpl implements CategoriesService {
    private CategoryRepository repository;

    @Autowired
    public CategoriesServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAll() {
        List<CategoryDto> result = new ArrayList<>();
        try {
            List<Object[]> list = repository.getCategories();
            list.forEach(l -> {
                CategoryDto post = new CategoryDto((Long) l[0], (String) l[1]);
                result.add(post);
            });
        } catch (ClassCastException exception) {
            throw new ClassCastException(exception.getMessage());
        }
        return result;
    }

    @Transactional(readOnly = true)
    public Set<CategoryDto> getTop5() {
        Set<CategoryDto> result = new LinkedHashSet<>();
        Set<Category> list = repository.findTop5ByOrderByPostListAscIdAsc();
        list = list.stream()
                .sorted(Comparator.comparing(Category::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        list.forEach(l -> {
            CategoryDto post = new CategoryDto(l.getId(), l.getName());
            result.add(post);
        });
        return result;
    }

    public Category create(CategoryDto categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        return repository.save(category);
    }

    public Category update(CategoryDto categoryDTO) {
        Category category = new Category();
        category.setId(category.getId());
        category.setName(categoryDTO.getName());
        return repository.save(category);
    }

    public boolean delete(Long id) {
        repository.deleteById(id);
        Optional<Category> category = repository.findById(id);
        return !category.isPresent();
    }
}

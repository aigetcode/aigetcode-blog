package com.blog.aigetcode.controllers;

import com.blog.aigetcode.DTO.CategoryDto;
import com.blog.aigetcode.entity.Category;
import com.blog.aigetcode.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

import static com.blog.aigetcode.entity.Authority.Role.Code.ADMIN;

@RestController
@RequestMapping("/category")
public class CategoriesController {
    private CategoriesService categoriesService;

    @Autowired
    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<CategoryDto> category = categoriesService.getAll();
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping("/top")
    public ResponseEntity<?> getTop5() {
        Set<CategoryDto> category = categoriesService.getTop5();
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping()
    @Secured({ADMIN})
    public ResponseEntity<?> create(@NonNull @RequestBody CategoryDto categoryDTO) {
        Category category = categoriesService.create(categoryDTO);
        return ResponseEntity.ok(category);
    }

    @PutMapping()
    @Secured({ADMIN})
    public ResponseEntity<?> update(@NonNull @RequestBody CategoryDto categoryDTO) {
        Category category = categoriesService.update(categoryDTO);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    @Secured({ADMIN})
    public ResponseEntity<?> delete(@NonNull @PathVariable("id") Long id) {
        boolean isDeleted = categoriesService.delete(id);
        return ResponseEntity.ok(isDeleted);
    }
}

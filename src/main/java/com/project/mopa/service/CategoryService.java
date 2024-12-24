package com.project.mopa.service;

import com.project.mopa.entity.Category;
import com.project.mopa.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories (){
        return categoryRepository.findAll();
    }
}

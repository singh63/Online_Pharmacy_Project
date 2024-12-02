package com.onlinepharmacy.service.services;

import com.onlinepharmacy.service.entities.Category;
import com.onlinepharmacy.service.payloads.CategoryDTO;
import com.onlinepharmacy.service.payloads.CategoryResponse;

public interface CategoryService {
    CategoryDTO createCategory(Category category);

    CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDTO updateCategory(Category category, Long categoryId);

    String deleteCategory(Long categoryId);
}

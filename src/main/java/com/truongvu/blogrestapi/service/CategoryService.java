package com.truongvu.blogrestapi.service;

import com.truongvu.blogrestapi.dto.CategoryDTO;
import com.truongvu.blogrestapi.entity.Category;

import java.util.List;

public interface CategoryService {
    CategoryDTO addCategory(CategoryDTO categoryDTO);
    CategoryDTO getCategory(long id);
    List<CategoryDTO> getAllCategory();
    CategoryDTO updateCategory(long id,CategoryDTO newCategoryDTO);
    void deleteCategory(long id);
}

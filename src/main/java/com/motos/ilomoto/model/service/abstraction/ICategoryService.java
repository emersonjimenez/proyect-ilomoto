package com.motos.ilomoto.model.service.abstraction;

import com.motos.ilomoto.model.dto.dto.APIResponse;
import com.motos.ilomoto.model.dto.dto.CategoryDto;
import com.motos.ilomoto.model.dto.dto.DataTableDTO;
import com.motos.ilomoto.model.dto.request.CategoryRequest;
import com.motos.ilomoto.model.dto.request.DataTableRequest;
import com.motos.ilomoto.model.entity.Category;

public interface ICategoryService {
    APIResponse<DataTableDTO<Category>> getCategoriesPage(DataTableRequest request);
    APIResponse<CategoryDto> createCategory(CategoryRequest categoryRequest);
    APIResponse<CategoryDto> getCategoryById(long id);
    APIResponse<CategoryDto> updateCategory(CategoryRequest categoryRequest, long id);
    APIResponse<CategoryDto> deleteCategory(long id);
    Category fetchById(long id);
}

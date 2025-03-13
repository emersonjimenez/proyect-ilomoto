package com.motos.ilomoto.controller.rest;

import com.motos.ilomoto.model.dto.dto.APIResponse;
import com.motos.ilomoto.model.dto.dto.CategoryDto;
import com.motos.ilomoto.model.dto.dto.DataTableDTO;
import com.motos.ilomoto.model.dto.request.CategoryRequest;
import com.motos.ilomoto.model.dto.request.DataTableRequest;
import com.motos.ilomoto.model.entity.Category;
import com.motos.ilomoto.model.service.abstraction.ICategoryService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
public class CategoryRestController {
    private final ICategoryService iCategoryService;

    @GetMapping
    public APIResponse<DataTableDTO<Category>> getCategoriesPage(
            @RequestParam(name = "draw", required = false, defaultValue = "1") int draw, // Identificador de la solicitud
            @RequestParam(name = "start", required = false, defaultValue = "0") int start, // Índice de inicio de los datos
            @RequestParam(name = "length", required = false, defaultValue = "5") int length, // Cantidad de registros por página
            @RequestParam(name = "search[value]", required = false, defaultValue = "") String searchValue, // Texto de búsqueda
            @RequestParam(name = "order[0][column]", required = false, defaultValue = "0") int orderColumn, // Columna para ordenar
            @RequestParam(name = "order[0][dir]", required = false, defaultValue = "asc") String orderDir // Dirección de orden (ASC/DESC)
    ) {
        return iCategoryService.getCategoriesPage(new DataTableRequest(draw, start, length, searchValue, orderColumn, orderDir));
    }

    @PostMapping
    public APIResponse<CategoryDto> createCategory(@RequestBody CategoryRequest categoryRequest) {
        return iCategoryService.createCategory(categoryRequest);
    }

    @GetMapping("/{id}")
    public APIResponse<CategoryDto> getCategoriesById(@PathVariable long id) {
        return iCategoryService.getCategoryById(id);
    }

    @PutMapping("/{id}")
    public APIResponse<CategoryDto> updateCategory(@RequestBody CategoryRequest categoryRequest, @PathVariable long id) {
        return iCategoryService.updateCategory(categoryRequest, id);
    }

    @DeleteMapping("/{id}")
    public APIResponse<CategoryDto> deleteCategory(@PathVariable long id) {
        return iCategoryService.deleteCategory(id);
    }
}

package com.motos.ilomoto.model.service.impl;

import com.motos.ilomoto.common.exception.CategoryException;
import com.motos.ilomoto.common.util.constant.CategoryConstant;
import com.motos.ilomoto.model.dto.dto.APIResponse;
import com.motos.ilomoto.model.dto.dto.CategoryDto;
import com.motos.ilomoto.model.dto.dto.DataTableDTO;
import com.motos.ilomoto.model.dto.request.CategoryRequest;
import com.motos.ilomoto.model.dto.request.DataTableRequest;
import com.motos.ilomoto.model.entity.Category;
import com.motos.ilomoto.model.mapper.dto.CategoryDtoMapper;
import com.motos.ilomoto.model.mapper.request.CategoryRequestMapper;
import com.motos.ilomoto.model.repository.CategoryRepository;
import com.motos.ilomoto.model.service.abstraction.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        rollbackFor = Exception.class
)
@Service
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryRequestMapper categoryRequestMapper;
    private final CategoryDtoMapper categoryDtoMapper;

    @Override
    @Transactional(readOnly = true)
    public APIResponse<DataTableDTO<Category>> getCategoriesPage(DataTableRequest request) {
        // Ordenar por la columna especificada
        Sort sort = Sort.by(
                request.getOrderDir().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
                mapIndexToColumnName(request.getOrderColumn())
        );

        // Configuración de la paginación
        int currentPage = request.getStart() / request.getLength();
        PageRequest pageRequest = PageRequest.of(currentPage, request.getLength(), sort);

        // Almacenará la paginación de categorias
        Page<Category> categoriesPage;

        // Filtrar si hay valor de búsqueda
        if (request.getSearchValue() != null && !request.getSearchValue().isEmpty()) {
            categoriesPage = categoryRepository.findByNameContainingIgnoreCase(request.getSearchValue(), pageRequest);
        } else {
            categoriesPage = categoryRepository.findAll(pageRequest);
        }

        // Crear el DTO de DataTable
        DataTableDTO<Category> dataTableDTO = new DataTableDTO<>(
                categoriesPage.getTotalElements(),
                categoriesPage.getTotalElements(),
                categoriesPage.getContent(),
                request.getDraw()
        );

        // Devolver el APIResponse con el DataTableDTO
        return new APIResponse<>(dataTableDTO, CategoryConstant.CATEGORY_LIST_SUCCESS, HttpStatus.OK);
    }

    @Override
    public APIResponse<CategoryDto> createCategory(CategoryRequest categoryRequest) {
        Category existingCategory = categoryRepository.findByName(categoryRequest.getName());
        if (existingCategory != null) {
            throw new CategoryException(
                    HttpStatus.CONFLICT,
                    String.format(CategoryConstant.CATEGORY_CREATION_CONFLICT, categoryRequest.getName())
            );
        };

        // Setter la categoria existente usando el mapper
        Category category = categoryRequestMapper.toEntity(categoryRequest);

        // Guardar una nueva categoria
        categoryRepository.save(category);

        // Respuesta de éxito
        return new APIResponse<>(null,
                String.format(CategoryConstant.CATEGORY_CREATION_SUCCESS, category.getName()),
                HttpStatus.CREATED
        );
    }

    @Override
    @Transactional(readOnly = true)
    public APIResponse<CategoryDto> getCategoryById(long id) {
        Category category = this.fetchById(id);

        //Respuesta de éxito
        return new APIResponse<>(
                categoryDtoMapper.toDto(category),
                String.format(CategoryConstant.CATEGORY_RETRIEVAL_SUCCESS, id),
                HttpStatus.OK
        );
    }

    @Override
    public APIResponse<CategoryDto> updateCategory(CategoryRequest categoryRequest, long id) {
        // Obtener la categoria existente
        Category existingCategory = this.fetchById(id);
        String categoryName = existingCategory.getName();

        // Verificar si el nombre de la categoria ya existe
        if (categoryRepository.existsByNameAndIdCategoryNot(categoryRequest.getName(), id)) {
            throw new CategoryException(
                    HttpStatus.CONFLICT,
                    String.format(CategoryConstant.CATEGORY_UPDATE_CONFLICT, categoryRequest.getName())
            );
        }

        // Setter la categoria existente usando el mapper
        Category category = categoryRequestMapper.toEntity(categoryRequest);
        category.setIdCategory(id);

        // Guardar la categoria actualizada
        categoryRepository.save(category);

        // Respuesta de éxito
        return new APIResponse<>(
                null,
                String.format(CategoryConstant.CATEGORY_UPDATE_SUCCESS, categoryName),
                HttpStatus.OK
        );
    }

    @Override
    public APIResponse<CategoryDto> deleteCategory(long id) {
        // Obtener la categoria existente
        Category category = this.fetchById(id);

//        // Verificar si el cliente tiene ventas asociadas.
//        if (categoryRepository.existsProductByIdCategory(id)) {
//            throw new CategoryException(
//                    HttpStatus.CONFLICT,
//                    String.format(CategoryConstant.CATEGORY_DELETE_CONFLICT, category.getName())
//            );
//        }

        // Eliminar la marca
        categoryRepository.delete(category);

        // Respuesta de éxito
        return new APIResponse<>(
                null,
                String.format(CategoryConstant.CATEGORY_DELETE_SUCCESS, category.getName()),
                HttpStatus.OK
        );
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Category fetchById(long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryException(
                HttpStatus.NOT_FOUND,
                String.format(CategoryConstant.CATEGORY_NOT_FOUND_ERROR, id)
        ));
    }

    /* Obtener el nombre de la columna basado en el índice */
    private String mapIndexToColumnName(int id) {
        return switch (id) {
            case 1 -> "name";
            default -> "idCategory"; // 0
        };
    }
}

package com.motos.ilomoto.model.mapper.dto;

import com.motos.ilomoto.model.dto.dto.CategoryDto;
import com.motos.ilomoto.model.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryDtoMapper {

    CategoryDto toDto(Category category);
}

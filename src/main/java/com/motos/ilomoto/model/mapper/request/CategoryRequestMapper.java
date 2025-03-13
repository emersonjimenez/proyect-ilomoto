package com.motos.ilomoto.model.mapper.request;

import com.motos.ilomoto.model.dto.request.CategoryRequest;
import com.motos.ilomoto.model.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryRequestMapper {

    Category toEntity(CategoryRequest categoryRequest);
}

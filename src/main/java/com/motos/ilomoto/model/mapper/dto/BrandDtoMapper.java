package com.motos.ilomoto.model.mapper.dto;

import com.motos.ilomoto.model.dto.dto.BrandDto;
import com.motos.ilomoto.model.entity.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") /* Define la interfaz como un mapper de MapStruct y componente de Spring. */
public interface BrandDtoMapper {

    /* Mapeo de campos de Marca a MarcaDto */
    BrandDto toDto(Brand brand); /* Se utiliza para responder al FrontEnd */
}

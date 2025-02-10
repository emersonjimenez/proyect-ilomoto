package com.motos.ilomoto.model.mapper.request;

import com.motos.ilomoto.model.dto.request.BrandRequest;
import com.motos.ilomoto.model.entity.Brand;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring") /* Define la interfaz como un mapper de MapStruct y componente de Spring.*/
public interface BrandRequestMapper {

    /* Mapeo de campos de MarcaRequest a Marca */
    Brand toEntity(BrandRequest brandRequest); /* Se utiliza para guardar en el BackEnd */
}

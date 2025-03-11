package com.motos.ilomoto.model.mapper.request;

import com.motos.ilomoto.model.dto.request.JobPositionRequest;
import com.motos.ilomoto.model.entity.JobPosition;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobPositionRequestMapper {

    /* Mapeo de campos de JobPositionRequest a PositionRequest */
    JobPosition toEntity(JobPositionRequest jobPositionRequest); /* Se utiliza para guardar en el BackEnd */
}

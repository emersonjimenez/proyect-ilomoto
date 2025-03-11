package com.motos.ilomoto.model.mapper.dto;

import com.motos.ilomoto.model.dto.dto.JobPositionDto;
import com.motos.ilomoto.model.entity.JobPosition;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobPositionDtoMapper {

    /* Mapeo de campos de JobPosition a JobPositionDto */
    JobPositionDto toDto(JobPosition jobPosition); /* Se utiliza para responder al FrontEnd */
}

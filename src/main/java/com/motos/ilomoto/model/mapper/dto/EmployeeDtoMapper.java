package com.motos.ilomoto.model.mapper.dto;

import com.motos.ilomoto.model.dto.dto.EmployeeDto;
import com.motos.ilomoto.model.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeDtoMapper {
    @Mapping(source = "idPosition.idPosition", target = "idPosition")
    @Mapping(source = "idDocumentType.idDocumentType", target = "idDocumentType")
    EmployeeDto toDto(Employee employee);//Se utiliza para convertir y mostrar en el FrontEnd
}

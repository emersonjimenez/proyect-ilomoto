package com.motos.ilomoto.model.mapper.request;

import com.motos.ilomoto.common.exception.DocumentTypeException;
import com.motos.ilomoto.common.exception.JobPositionException;
import com.motos.ilomoto.common.util.constant.DocumentTypeConstant;
import com.motos.ilomoto.model.dto.request.EmployeeRequest;
import com.motos.ilomoto.model.entity.DocumentType;
import com.motos.ilomoto.model.entity.Employee;
import com.motos.ilomoto.model.entity.JobPosition;
import com.motos.ilomoto.model.repository.DocumentTypeRepository;
import com.motos.ilomoto.model.repository.JobPositionRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@Mapper(componentModel = "spring")
public abstract class EmployeeRequestMapper {
    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private JobPositionRepository jobPositionRepository;

    @Mapping(source = "idDocumentType", target = "idDocumentType", qualifiedByName = "mapToDocumentType")
    @Mapping(source = "idPosition", target = "idPosition", qualifiedByName = "mapToJobPosition")
    public abstract Employee toEntity(EmployeeRequest request);//Se utiliza para convertir y guardar en el BackEnd

    @Named("mapToDocumentType")
    public DocumentType mapToDocumentType(Long idDocumentType) {
        if (idDocumentType == null) {
            return null;
        }
        return documentTypeRepository.findById(idDocumentType)
                .orElseThrow(() -> new DocumentTypeException(
                        HttpStatus.NOT_FOUND,
                        String.format(DocumentTypeConstant.DOCUMENT_TYPE_NOT_FOUND_ERROR,idDocumentType)
                ));
    }

    @Named("mapToJobPosition")
    public JobPosition mapToJobPosition(Long idPosition) {
        if (idPosition == null) {
            return null;
        }
        return jobPositionRepository.findById(idPosition)
                .orElseThrow(() -> new JobPositionException(
                        HttpStatus.NOT_FOUND,
                        String.format(DocumentTypeConstant.DOCUMENT_TYPE_NOT_FOUND_ERROR,idPosition)
                ));
    }
}

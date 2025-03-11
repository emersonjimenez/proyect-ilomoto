package com.motos.ilomoto.model.service.impl;

import com.motos.ilomoto.common.exception.JobPositionException;
import com.motos.ilomoto.common.util.constant.JobPositionConstant;
import com.motos.ilomoto.model.dto.dto.APIResponse;
import com.motos.ilomoto.model.dto.dto.DataTableDTO;
import com.motos.ilomoto.model.dto.dto.JobPositionDto;
import com.motos.ilomoto.model.dto.request.DataTableRequest;
import com.motos.ilomoto.model.dto.request.JobPositionRequest;
import com.motos.ilomoto.model.entity.JobPosition;
import com.motos.ilomoto.model.mapper.dto.JobPositionDtoMapper;
import com.motos.ilomoto.model.mapper.request.JobPositionRequestMapper;
import com.motos.ilomoto.model.repository.JobPositionRepository;
import com.motos.ilomoto.model.service.abstraction.IJobPositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(
        isolation = Isolation.READ_COMMITTED,
        rollbackFor = Exception.class
)
@Service
public class JobPositionServiceImpl implements IJobPositionService {
    private final JobPositionRepository jobPositionRepository;
    private final JobPositionRequestMapper jobPositionRequestMapper;
    private final JobPositionDtoMapper jobPositionDtoMapper;

    @Override
    @Transactional(readOnly = true)
    public APIResponse<DataTableDTO<JobPosition>> getJobPositionsPage(DataTableRequest request) {
        // Ordenar por la columna especificada
        Sort sort = Sort.by(
                request.getOrderDir().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
                mapIndexToColumnName(request.getOrderColumn())
        );

        // Configuración de la paginación
        int currentPage = request.getStart() / request.getLength();
        Pageable pageable = PageRequest.of(currentPage, request.getLength(), sort);

        // Almacenará la paginación de marcas
        Page<JobPosition> jobPositionsPage;

        // Filtrar si hay valor de búsqueda
        if (request.getSearchValue() != null && !request.getSearchValue().isEmpty()) {
            jobPositionsPage = jobPositionRepository.findByNameContainingIgnoreCase(request.getSearchValue(), pageable);
        } else {
            jobPositionsPage = jobPositionRepository.findAll(pageable);
        }

        // Crear el DTO de DataTable
        DataTableDTO<JobPosition> dataTableDTO = new DataTableDTO<>(
                jobPositionsPage.getTotalElements(),
                jobPositionsPage.getTotalElements(),
                jobPositionsPage.getContent(),
                request.getDraw()
        );

        // Devolver el APIResponse con el DataTableDTO
        return new APIResponse<>(
                dataTableDTO,
                JobPositionConstant.JOB_POSITION_LIST_SUCCESS,
                HttpStatus.OK
        );
    }

        @Override
        public APIResponse<List<JobPositionDto>> getJobPositions() {
            List<JobPosition> jobPositionList =  jobPositionRepository.findAll();
            if (jobPositionList.isEmpty()) {
                return new APIResponse<>(
                        null,
                        JobPositionConstant.JOB_POSITION_EMPTY_LIST_ERROR,
                        HttpStatus.OK
                );
            }

            // Convertir la lista de JobPosition a JobPositionDto
            List<JobPositionDto> jobPositionDtoList = jobPositionList.stream()
                    .map(jobPositionDtoMapper::toDto)
                    .collect(Collectors.toList());

            return new APIResponse<>(
                    jobPositionDtoList,
                    JobPositionConstant.JOB_POSITION_LIST_SUCCESS,
                    HttpStatus.OK
            );
        }

    @Override
    public APIResponse<JobPositionDto> createJobPosition(JobPositionRequest jobPositionRequest) {
        // Validar si el cargo empleado ya existe
        JobPosition existingJobPosition =  jobPositionRepository.findByName(jobPositionRequest.getName());
        if (existingJobPosition != null) {
            throw new JobPositionException(
              HttpStatus.CONFLICT,
              String.format(JobPositionConstant.JOB_POSITION_CREATION_CONFLICT, jobPositionRequest.getName())
            );
        }

        // Setter el cargo empleado existente usando el mapper
        JobPosition jobPosition = jobPositionRequestMapper.toEntity(jobPositionRequest);

        // Guardar un nuevo cargo empleado
        jobPositionRepository.save(jobPosition);

        // Respuesta de éxito
        return new APIResponse<>(null,
                String.format(JobPositionConstant.JOB_POSITION_CREATION_SUCCESS, jobPosition.getName()),
                HttpStatus.CREATED
        );
    }

    @Override
    @Transactional(readOnly = true)
    public APIResponse<JobPositionDto> getJobPositionById(long id) {
        JobPosition jobPosition = this.fetchById(id);

        //Respuesta con éxito
        return new APIResponse<>(
                jobPositionDtoMapper.toDto(jobPosition),
                String.format(JobPositionConstant.JOB_POSITION_RETRIEVAL_SUCCESS,id),
                HttpStatus.OK
        );
    }

    @Override
    public APIResponse<JobPositionDto> updateJobPosition(JobPositionRequest jobPositionRequest, long id) {
        // Obtener el cargo de empleado existente
        JobPosition existingJobPosition = this.fetchById(id);
        String positionName = existingJobPosition.getName();

        // Verificar si el nombre de cargo empleado ya existe
        if (jobPositionRepository.existsByNameAndIdPositionNot(jobPositionRequest.getName(), id)) {
            throw new JobPositionException(
                    HttpStatus.CONFLICT,
                    String.format(JobPositionConstant.JOB_POSITION_UPDATE_CONFLICT, jobPositionRequest.getName())
            );
        }

        // Setter el cargo empleado existente usando el mapper
        JobPosition jobPosition = jobPositionRequestMapper.toEntity(jobPositionRequest);
        jobPosition.setIdPosition(id);

        // Guardar el cargo empleado actualizado
        jobPositionRepository.save(jobPosition);

        // Respuesta de éxito
        return new APIResponse<>(
                null,
                String.format(JobPositionConstant.JOB_POSITION_UPDATE_SUCCESS, positionName),
                HttpStatus.OK
        );
    }

    @Override
    public APIResponse<JobPositionDto> deleteJobPosition(long id) {
        // Obtener el cargo empleado existente
        JobPosition jobPosition = this.fetchById(id);

        /* Verificar si el cargo de empleado tiene empleados asociados.
        if (jobPositionRepository.existsEmployeeByPositionId(id)) {
            throw new JobPositionException(
                    HttpStatus.CONFLICT,
                    String.format(JobPositionConstant.JOB_POSITION_DELETE_CONFLICT, jobPosition.getName())
            );
        }*/

        //Eliminar el cargo de empleado
        jobPositionRepository.delete(jobPosition);

        return new APIResponse<>(
                null,
                String.format(JobPositionConstant.JOB_POSITION_DELETE_SUCCESS,jobPosition.getName()),
                HttpStatus.OK
        );
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED) // Reutiliza la transacción existente
    public JobPosition fetchById(long id) {
        return jobPositionRepository.findById(id).orElseThrow(()-> new JobPositionException(
                HttpStatus.NOT_FOUND,
                String.format(JobPositionConstant.JOB_POSITION_NOT_FOUND_ERROR,id)
        ));
    }

    /* Obtener el nombre de la columna basado en el índice */
    private String mapIndexToColumnName(int id) {
        return switch (id) {
            case 1 -> "name";
            default -> "idPosition"; // 0
        };
    }
}

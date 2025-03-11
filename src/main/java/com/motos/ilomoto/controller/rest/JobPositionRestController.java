package com.motos.ilomoto.controller.rest;

import com.motos.ilomoto.model.dto.dto.APIResponse;
import com.motos.ilomoto.model.dto.dto.DataTableDTO;
import com.motos.ilomoto.model.dto.dto.JobPositionDto;
import com.motos.ilomoto.model.dto.request.DataTableRequest;
import com.motos.ilomoto.model.dto.request.JobPositionRequest;
import com.motos.ilomoto.model.entity.JobPosition;
import com.motos.ilomoto.model.service.abstraction.IJobPositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/job-position")
public class JobPositionRestController {
    private final IJobPositionService iJobPositionService;

    @GetMapping
    public APIResponse<DataTableDTO<JobPosition>> getJobPositionsPage(
            @RequestParam(name = "draw", required = false, defaultValue = "1") int draw, // Identificador de la solicitud
            @RequestParam(name = "start", required = false, defaultValue = "0") int start, // Índice de inicio de los datos
            @RequestParam(name = "length", required = false, defaultValue = "5") int length, // Cantidad de registros por página
            @RequestParam(name = "search[value]", required = false, defaultValue = "") String searchValue, // Texto de búsqueda
            @RequestParam(name = "order[0][column]", required = false, defaultValue = "0") int orderColumn, // Columna para ordenar
            @RequestParam(name = "order[0][dir]", required = false, defaultValue = "asc") String orderDir // Dirección de orden (ASC/DESC)
    ) {
        return iJobPositionService.getJobPositionsPage(new DataTableRequest(draw, start, length, searchValue, orderColumn, orderDir));
    }

    @GetMapping("/list")
    public APIResponse<List<JobPositionDto>> getJobPositions() {
        return iJobPositionService.getJobPositions();
    }

    @PostMapping
    public APIResponse<JobPositionDto> createJobPosition(@RequestBody JobPositionRequest jobPositionRequest) {
        return iJobPositionService.createJobPosition(jobPositionRequest);
    }

    @GetMapping("/{id}")
    public APIResponse<JobPositionDto> getJobPositionById(@PathVariable long id) {
        return iJobPositionService.getJobPositionById(id);
    }

    @PutMapping("/{id}")
    public APIResponse<JobPositionDto> updateJobPosition(@RequestBody JobPositionRequest jobPositionRequest, @PathVariable long id) {
        return iJobPositionService.updateJobPosition(jobPositionRequest, id);
    }

    @DeleteMapping("/{id}")
    public APIResponse<JobPositionDto> deleteJobPosition(@PathVariable long id) {
        return iJobPositionService.deleteJobPosition(id);
    }
}

package com.motos.ilomoto.model.service.abstraction;

import com.motos.ilomoto.model.dto.dto.APIResponse;
import com.motos.ilomoto.model.dto.dto.DataTableDTO;
import com.motos.ilomoto.model.dto.dto.JobPositionDto;
import com.motos.ilomoto.model.dto.request.DataTableRequest;
import com.motos.ilomoto.model.dto.request.JobPositionRequest;
import com.motos.ilomoto.model.entity.JobPosition;

import java.util.List;

public interface IJobPositionService {
    APIResponse<DataTableDTO<JobPosition>> getJobPositionsPage(DataTableRequest request);
    APIResponse<List<JobPositionDto>> getJobPositions();
    APIResponse<JobPositionDto> createJobPosition(JobPositionRequest jobPositionRequest);
    APIResponse<JobPositionDto> getJobPositionById(long id);
    APIResponse<JobPositionDto> updateJobPosition(JobPositionRequest jobPositionRequest, long id);
    APIResponse<JobPositionDto> deleteJobPosition(long id);
    JobPosition fetchById(long id);
}

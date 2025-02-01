package com.motos.ilomoto.model.service.abstraction;

import com.motos.ilomoto.model.dto.dto.APIResponse;
import com.motos.ilomoto.model.dto.dto.BrandDto;
import com.motos.ilomoto.model.dto.request.BrandRequest;
import com.motos.ilomoto.model.entity.Brand;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface IBrandService {
    ResponseEntity<Map<String, Object>> findAllBrands(long offset, long pageSize, String searchQuery, long sortColum, String sortDirection, long requestId);
    APIResponse<BrandDto> createBrand(BrandRequest brandRequest);
    APIResponse<BrandDto> findBrandById(long id);
    APIResponse<BrandDto> updateBrand(BrandRequest brandRequest, long id);
    APIResponse<BrandDto> deleteBrand(long id);
    Brand fetchById(long id);
}

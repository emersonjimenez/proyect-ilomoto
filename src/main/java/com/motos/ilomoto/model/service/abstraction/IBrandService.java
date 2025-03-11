package com.motos.ilomoto.model.service.abstraction;

import com.motos.ilomoto.model.dto.dto.APIResponse;
import com.motos.ilomoto.model.dto.dto.BrandDto;
import com.motos.ilomoto.model.dto.dto.DataTableDTO;
import com.motos.ilomoto.model.dto.request.BrandRequest;
import com.motos.ilomoto.model.dto.request.DataTableRequest;
import com.motos.ilomoto.model.entity.Brand;

public interface IBrandService {
    APIResponse<DataTableDTO<Brand>> getBrandsPage(DataTableRequest request);
    APIResponse<BrandDto> createBrand(BrandRequest brandRequest);
    APIResponse<BrandDto> getBrandById(long id);
    APIResponse<BrandDto> updateBrand(BrandRequest brandRequest, long id);
    APIResponse<BrandDto> deleteBrand(long id);
    Brand findBrandByIdOrThrow(long id);
}

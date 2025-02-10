package com.motos.ilomoto.controller.rest;

import com.motos.ilomoto.model.dto.dto.APIResponse;
import com.motos.ilomoto.model.dto.dto.BrandDto;
import com.motos.ilomoto.model.dto.dto.DataTableDTO;
import com.motos.ilomoto.model.dto.request.BrandRequest;
import com.motos.ilomoto.model.dto.request.DataTableRequest;
import com.motos.ilomoto.model.entity.Brand;
import com.motos.ilomoto.model.service.abstraction.IBrandService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/brand")
public class BrandRestController {
    private final IBrandService iBrandService; /* Inyección por constructor usando Lombok. */

    @GetMapping
    public APIResponse<DataTableDTO<Brand>> getBrandsPage(
            @RequestParam(name = "draw", required = false, defaultValue = "1") int draw, // Identificador de la solicitud
            @RequestParam(name = "start", required = false, defaultValue = "0") int start, // Índice de inicio de los datos
            @RequestParam(name = "length", required = false, defaultValue = "5") int length, // Cantidad de registros por página
            @RequestParam(name = "search[value]", required = false, defaultValue = "") String searchValue, // Texto de búsqueda
            @RequestParam(name = "order[0][column]", required = false, defaultValue = "0") int orderColumn, // Columna para ordenar
            @RequestParam(name = "order[0][dir]", required = false, defaultValue = "asc") String orderDir // Dirección de orden (ASC/DESC)
    ) {
        return iBrandService.getBrandsPage(new DataTableRequest(draw, start, length, searchValue, orderColumn, orderDir));
    }

    @PostMapping
    public APIResponse<BrandDto> createBrand(@RequestBody BrandRequest brandRequest) {
        return iBrandService.createBrand(brandRequest);
    }

    @GetMapping("/{id}")
    public APIResponse<BrandDto> getBrandById(@PathVariable long id) {
        return iBrandService.getBrandById(id);
    }

    @PutMapping("/{id}")
    public APIResponse<BrandDto> updateBrand(@RequestBody BrandRequest brandRequest, @PathVariable long id) {
        return iBrandService.updateBrand(brandRequest, id);
    }

    @DeleteMapping("/{id}")
    public APIResponse<BrandDto> deleteBrand(@PathVariable long id) {
        return iBrandService.deleteBrand(id);
    }
}

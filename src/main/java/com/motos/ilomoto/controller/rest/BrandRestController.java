package com.motos.ilomoto.controller.rest;

import com.motos.ilomoto.model.dto.dto.APIResponse;
import com.motos.ilomoto.model.dto.dto.BrandDto;
import com.motos.ilomoto.model.dto.request.BrandRequest;
import com.motos.ilomoto.model.service.abstraction.IBrandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@RestController /* Índica que será un controlador que manejara solicitudes entrantes y respuestas. */
@RequestMapping("/brand") /* Es para conectar las peticiones web con los métodos del controlador. */
public class BrandRestController {
    private final IBrandService iBrandService;  /* Inyección por constructor usando Lombok. */

    @PostMapping("/list")
    public ResponseEntity<Map<String,Object>> findAllBrands(
            @RequestParam("start") long offset, /* índice de inicio de los datos  */
            @RequestParam("length") long pageSize, /* Cantidad de registros por página */
            @RequestParam("search[value]") String searchQuery, /* Texto de búsqueda */
            @RequestParam("order[0][column]") long sortColum, /* Columna para ordenar */
            @RequestParam("order[0][dir]") String sortDirection, /* Dirección de orden (ASC/DESC) */
            @RequestParam("draw") long requestId) { /* Identificador de la solicitud */
        return iBrandService.findAllBrands(offset,pageSize,searchQuery,sortColum,sortDirection,requestId);
    }

    @PostMapping("/add")
    public APIResponse<BrandDto> createBrand(@RequestBody BrandRequest brandRequest) {
        return iBrandService.createBrand(brandRequest);
    }

    @GetMapping("/findById/{id}")
    public APIResponse<BrandDto> findBrandById(@PathVariable long id) {
        return iBrandService.findBrandById(id);
    }

    @PutMapping("/update/{id}")
    public APIResponse<BrandDto> updateBrand(@RequestBody BrandRequest brandRequest, @PathVariable long id) {
        return iBrandService.updateBrand(brandRequest, id);
    }

    @DeleteMapping("/delete/{id}")
    public APIResponse<BrandDto> deleteBrand(@PathVariable long id) {
        return iBrandService.deleteBrand(id);
    }
}

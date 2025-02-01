package com.motos.ilomoto.model.service.impl;

import com.motos.ilomoto.common.exception.BrandException;
import com.motos.ilomoto.common.util.constant.BrandConstant;
import com.motos.ilomoto.model.dto.dto.APIResponse;
import com.motos.ilomoto.model.dto.dto.BrandDto;
import com.motos.ilomoto.model.dto.request.BrandRequest;
import com.motos.ilomoto.model.entity.Brand;
import com.motos.ilomoto.model.mapper.dto.BrandDtoMapper;
import com.motos.ilomoto.model.mapper.request.BrandRequestMapper;
import com.motos.ilomoto.model.repository.BrandRepository;
import com.motos.ilomoto.model.service.abstraction.IBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(
        isolation = Isolation.READ_COMMITTED, /* Evita leer datos no confirmados. */
        rollbackFor = Exception.class /* Realiza rollback para cualquier excepción. */
)
public class BrandServiceImpl implements IBrandService {
    private final BrandRepository brandRepository;
    private final BrandRequestMapper brandRequestMapper;
    private final BrandDtoMapper brandDtoMapper;

    @Override
    @Transactional(readOnly = true)/* Transacción solo para lecturas */
    public ResponseEntity<Map<String, Object>> findAllBrands(long offset, long pageSize, String searchQuery, long sortColum, String sortDirection, long requestId) {
        Map<String, Object> response = new HashMap<>();

        // Ordenar por la columna especificada
        var sort = Sort.by(
                sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
                mapIndexToColumnName(sortColum)
        );

        // Configuración de la paginación
        var currentPage = offset / pageSize;
        PageRequest pageRequest = PageRequest.of((int) currentPage, (int) pageSize, sort);

        // Almacenará la paginación de marcas
        Page<Brand> brandsPage;

        // Filtrar si hay valor de búsqueda
        if (searchQuery != null && !searchQuery.isEmpty()) {
            brandsPage = brandRepository.findByNameContainingIgnoreCase(searchQuery, pageRequest);
        } else {
            brandsPage = brandRepository.findAll(pageRequest);
        }

        // Configuración para DataTables
        response.put("recordsTotal", brandsPage.getTotalElements());
        response.put("recordsFiltered", brandsPage.getTotalElements());
        response.put("data", brandsPage.getContent());
        response.put("draw", requestId);
        response.put("message", BrandConstant.BRAND_LIST_SUCCESS);

        return ResponseEntity.ok(response);
    }

    @Override
    public APIResponse<BrandDto> createBrand(BrandRequest brandRequest) {
        // Validar si la marca ya existe
        var existingBrand = brandRepository.findByName(brandRequest.getName());
        if (existingBrand != null) {
            throw new BrandException(
                    HttpStatus.CONFLICT,
                    String.format(BrandConstant.BRAND_CREATION_CONFLICT, brandRequest.getName())
            );
        }

        // Crear una nueva marca
        var newBrand = brandRequestMapper.brandRequestToBrand(brandRequest);
        var savedBrand = brandRepository.save(newBrand);

        // Respuesta de éxito
        return new APIResponse<>(
                brandDtoMapper.brandToBrandDto(savedBrand),
                String.format(BrandConstant.BRAND_CREATION_SUCCESS, savedBrand.getName()),
                HttpStatus.CREATED
        );
    }

    @Override
    @Transactional(readOnly = true)
    public APIResponse<BrandDto> findBrandById(long id) {
        var brand = this.fetchById(id);
        return new APIResponse<>(
                brandDtoMapper.brandToBrandDto(brand),
                String.format(BrandConstant.BRAND_RETRIEVAL_SUCCESS, id),
                HttpStatus.OK
        );
    }

    @Override
    public APIResponse<BrandDto> updateBrand(BrandRequest brandRequest, long id) {
        // Obtener la marca existente
        var brand = this.fetchById(id);

        // Validar si la marca ya existe
        var existingBrand = brandRepository.findByName(brandRequest.getName());

        if (existingBrand != null && id != existingBrand.getIdBrand()) {
            throw new BrandException(
                    HttpStatus.CONFLICT,
                    String.format(BrandConstant.BRAND_UPDATE_CONFLICT, id)
            );
        }

        // Actualizar la marca
        setValuesBrand(brandRequest, brand);
        var savedBrand = brandRepository.save(brand);

        // Respuesta de éxito
        return new APIResponse<>(
                brandDtoMapper.brandToBrandDto(savedBrand),
                String.format(BrandConstant.BRAND_UPDATE_SUCCESS, savedBrand.getName()),
                HttpStatus.OK
        );
    }

    @Override
    public APIResponse<BrandDto> deleteBrand(long id) {
        var brand = this.fetchById(id);

        /* Verificar si el cliente tiene ventas asociadas.
        if (brandRepository.existsProductByBrandId(id)) {
            throw new BrandException(
                    HttpStatus.CONFLICT,
                    String.format(BrandConstant.BRAND_DELETE_CONFLICT, id)
            );
        }*/

        brandRepository.delete(brand);

        return new APIResponse<>(
                null,
                String.format(BrandConstant.BRAND_DELETE_SUCCESS, id),
                HttpStatus.OK
        );
    }

    /* Buscar marca por ID con validación */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED) // Reutiliza la transacción existente
    public Brand fetchById(long id) {
        return brandRepository.findById(id).orElseThrow(() -> new BrandException(
                HttpStatus.NOT_FOUND,
                String.format(BrandConstant.BRAND_NOT_FOUND_ERROR, id)
        ));
    }

    /* Obtener el nombre de la columna basado en el índice */
    private String mapIndexToColumnName(long id) {
        return switch ((int) id) {
            case 1 -> "name";
            default -> "idBrand"; // 0
        };
    }

    /* Actualizar atributos de la marca */
    private void setValuesBrand(BrandRequest brandRequest, Brand brand) {
        brand.setName(brandRequest.getName());
    }
}

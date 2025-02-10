package com.motos.ilomoto.model.service.impl;

import com.motos.ilomoto.common.exception.BrandException;
import com.motos.ilomoto.common.util.constant.BrandConstant;
import com.motos.ilomoto.model.dto.dto.APIResponse;
import com.motos.ilomoto.model.dto.dto.BrandDto;
import com.motos.ilomoto.model.dto.dto.DataTableDTO;
import com.motos.ilomoto.model.dto.request.BrandRequest;
import com.motos.ilomoto.model.dto.request.DataTableRequest;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(
        isolation = Isolation.READ_COMMITTED, /* Evita leer datos no confirmados. */
        rollbackFor = Exception.class /* Realiza rollback para cualquier excepción. */
)
@Service
public class BrandServiceImpl implements IBrandService {
    private final BrandRepository brandRepository;
    private final BrandRequestMapper brandRequestMapper;
    private final BrandDtoMapper brandDtoMapper;

    @Override
    @Transactional(readOnly = true) /* Transacción solo para lecturas */
    public APIResponse<DataTableDTO<Brand>> getBrandsPage(DataTableRequest request) {
        // Ordenar por la columna especificada
        Sort sort = Sort.by(
                request.getOrderDir().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
                mapIndexToColumnName(request.getOrderColumn())
        );

        // Configuración de la paginación
        int currentPage = request.getStart() / request.getLength();
        PageRequest pageRequest = PageRequest.of(currentPage, request.getLength(), sort);

        // Almacenará la paginación de marcas
        Page<Brand> brandsPage;

        // Filtrar si hay valor de búsqueda
        if (request.getSearchValue() != null && !request.getSearchValue().isEmpty()) {
            brandsPage = brandRepository.findByNameContainingIgnoreCase(request.getSearchValue(), pageRequest);
        } else {
            brandsPage = brandRepository.findAll(pageRequest);
        }

        // Crear el DTO de DataTable
        DataTableDTO<Brand> dataTableDTO = new DataTableDTO<>(
                brandsPage.getTotalElements(),
                brandsPage.getTotalElements(),
                brandsPage.getContent(),
                request.getDraw()
        );

        // Devolver el APIResponse con el DataTableDTO
        return new APIResponse<>(dataTableDTO, BrandConstant.BRAND_LIST_SUCCESS, HttpStatus.OK);
    }

    @Override
    public APIResponse<BrandDto> createBrand(BrandRequest brandRequest) {
        // Validar si la marca ya existe
        Brand existingBrand = brandRepository.findByName(brandRequest.getName());
        if (existingBrand != null) {
            throw new BrandException(
                    HttpStatus.CONFLICT,
                    String.format(BrandConstant.BRAND_CREATION_CONFLICT, brandRequest.getName())
            );
        }

        // Setter la marca existente usando el mapper
        Brand brand = brandRequestMapper.toEntity(brandRequest);

        // Guardar una nueva marca
        brandRepository.save(brand);

        // Respuesta de éxito
        return new APIResponse<>(null,
                String.format(BrandConstant.BRAND_CREATION_SUCCESS, brand.getName()),
                HttpStatus.CREATED
        );
    }

    @Override
    @Transactional(readOnly = true)
    public APIResponse<BrandDto> getBrandById(long id) {
        Brand brand = this.fetchById(id);

        //Respuesta de éxito
        return new APIResponse<>(
                brandDtoMapper.toDto(brand),
                String.format(BrandConstant.BRAND_RETRIEVAL_SUCCESS, id),
                HttpStatus.OK
        );
    }

    @Override
    public APIResponse<BrandDto> updateBrand(BrandRequest brandRequest, long id) {
        // Obtener la marca existente
        Brand existingBrand = this.fetchById(id);
        String brandName = existingBrand.getName();

        // Verificar si el nombre ya existe en otra marca
        if (brandRepository.existsByNameAndIdBrandNot(brandRequest.getName(), id)) {
            throw new BrandException(
                    HttpStatus.CONFLICT,
                    String.format(BrandConstant.BRAND_UPDATE_CONFLICT, brandRequest.getName())
            );
        }

        // Setter la marca existente usando el mapper
        Brand brand = brandRequestMapper.toEntity(brandRequest);
        brand.setIdBrand(id);

        // Guardar la marca actualizada
        brandRepository.save(brand);

        // Respuesta de éxito
        return new APIResponse<>(
                null,
                String.format(BrandConstant.BRAND_UPDATE_SUCCESS, brandName),
                HttpStatus.OK
        );
    }

    @Override
    public APIResponse<BrandDto> deleteBrand(long id) {
        // Obtener la marca existente
        Brand brand = this.fetchById(id);

        /* Verificar si el cliente tiene ventas asociadas.
        if (brandRepository.existsProductByBrandId(id)) {
            throw new BrandException(
                    HttpStatus.CONFLICT,
                    String.format(BrandConstant.BRAND_DELETE_CONFLICT, brand.getName())
            );
        }*/

        // Eliminar la marca
        brandRepository.delete(brand);

        // Respuesta de éxito
        return new APIResponse<>(
                null,
                String.format(BrandConstant.BRAND_DELETE_SUCCESS, brand.getName()),
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
    private String mapIndexToColumnName(int id) {
        return switch (id) {
            case 1 -> "name";
            default -> "idBrand"; // 0
        };
    }
}

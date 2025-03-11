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
import org.springframework.data.domain.Pageable;
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

    // Obtiene una página de marcas con filtrado y ordenación
    @Override
    @Transactional(readOnly = true) /* Transacción solo para lecturas */
    public APIResponse<DataTableDTO<Brand>> getBrandsPage(DataTableRequest request) {
        Sort sort = createSort(request);
        Pageable pageable = createPageable(request, sort);
        Page<Brand> brandsPage = retrieveBrands(request, pageable);

        return new APIResponse<>(
                buildDataTableResponse(brandsPage, request),
                BrandConstant.BRAND_LIST_SUCCESS,
                HttpStatus.OK
        );
    }

    // Crea el objeto Sort según el orden indicado
    private Sort createSort(DataTableRequest request) {
        return Sort.by(
                request.getOrderDir().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
                mapColumnIndexToColumnName(request.getOrderColumn())
        );
    }

    // Mapea el índice de columna a su nombre correspondiente
    private String mapColumnIndexToColumnName(int columnIndex) {
        return columnIndex == 1 ? "name" : "idBrand";
    }

    // Configura la paginación
    private Pageable createPageable(DataTableRequest request, Sort sort) {
        int currentPage = request.getStart() / request.getLength();
        return PageRequest.of(currentPage, request.getLength(), sort);
    }

    // Recupera las marcas, aplicando búsqueda por nombre
    private Page<Brand> retrieveBrands(DataTableRequest request, Pageable pageable) {
        if (request.getSearchValue() != null && !request.getSearchValue().isEmpty()) {
            return brandRepository.findByNameContainingIgnoreCase(request.getSearchValue(), pageable);
        }
        return brandRepository.findAll(pageable);
    }

    // Construye el DTO de la tabla de datos
    private DataTableDTO<Brand> buildDataTableResponse(Page<Brand> brandsPage, DataTableRequest request) {
        return new DataTableDTO<>(
                brandsPage.getTotalElements(),
                brandsPage.getTotalElements(),
                brandsPage.getContent(),
                request.getDraw()
        );
    }

    // Crea una nueva marca
    @Override
    public APIResponse<BrandDto> createBrand(BrandRequest brandRequest) {
        // Validar si la marca ya existe
        ensureBrandNameIsUnique(brandRequest.getName());

        // Setter la marca existente usando el mapper
        Brand brand = brandRequestMapper.toEntity(brandRequest);

        brandRepository.save(brand);
        return new APIResponse<>(
                null,
                String.format(BrandConstant.BRAND_CREATION_SUCCESS,
                brand.getName()), HttpStatus.CREATED
        );
    }

    // Verífica que no exista otra marca con el mismo nombre
    private void ensureBrandNameIsUnique(String name) {
        if (brandRepository.findByName(name) != null) {
            throw new BrandException(
                    HttpStatus.CONFLICT,
                    String.format(BrandConstant.BRAND_CREATION_CONFLICT,name)
            );
        }
    }

    // Obtiene una marca por ID
    @Override
    @Transactional(readOnly = true)
    public APIResponse<BrandDto> getBrandById(long id) {
        Brand brand = this.findBrandByIdOrThrow(id);

        return new APIResponse<>(
                brandDtoMapper.toDto(brand),
                String.format(BrandConstant.BRAND_RETRIEVAL_SUCCESS, id),
                HttpStatus.OK
        );
    }

    // Actualiza una marca
    @Override
    public APIResponse<BrandDto> updateBrand(BrandRequest brandRequest, long id) {
        // Obtener la marca existente
        Brand existingBrand = this.findBrandByIdOrThrow(id);
        String brandName = existingBrand.getName();

        // Verificar si el nombre de la marca ya existe
        ensureBrandNameIsUnique(brandRequest.getName(), id);

        // Setter la marca existente usando el mapper
        Brand brand = brandRequestMapper.toEntity(brandRequest);
        brand.setIdBrand(id);

        brandRepository.save(brand);
        return new APIResponse<>(
                null,
                String.format(BrandConstant.BRAND_UPDATE_SUCCESS, brandName),
                HttpStatus.OK
        );
    }

    // Verífica que no exista otra marca con el mismo nombre al actualizar
    private void ensureBrandNameIsUnique(String name, long id) {
        if (brandRepository.existsByNameAndIdBrandNot(name, id)) {
            throw new BrandException(
                    HttpStatus.CONFLICT,
                    String.format(BrandConstant.BRAND_UPDATE_CONFLICT, name)
            );
        }
    }

    // Elimina una marca
    @Override
    public APIResponse<BrandDto> deleteBrand(long id) {
        // Obtener la marca existente
        Brand brand = this.findBrandByIdOrThrow(id);

        // Verificar si el cliente tiene ventas asociadas.
        ensureBrandHasNoDependencies(id);

        brandRepository.delete(brand);
        return new APIResponse<>(
                null,
                String.format(BrandConstant.BRAND_DELETE_SUCCESS, brand.getName()),
                HttpStatus.OK
        );
    }

    // Verífica dependencia antes de eliminar
    private void ensureBrandHasNoDependencies(long id) {
        /*
        if (brandRepository.existsProductByBrandId(id)) {
            throw new BrandException(
                HttpStatus.CONFLICT,
                String.format(BrandConstant.BRAND_DELETE_CONFLICT, brand.getName()
            ));
        }*/
    }

    // Busca una marca por ID o lanza excepción si no existe
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED) // Reutiliza la transacción existente
    public Brand findBrandByIdOrThrow(long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new BrandException(
                        HttpStatus.NOT_FOUND,
                        String.format(BrandConstant.BRAND_NOT_FOUND_ERROR, id)
                ));
    }
}

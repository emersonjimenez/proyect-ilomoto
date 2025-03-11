package com.motos.ilomoto.model.repository;

import com.motos.ilomoto.model.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    // Obtener marcas con paginación y ordenación
    Page<Brand> findAll(@NonNull Pageable pageable);

    // Buscar marca por nombre con paginación y ordenación
    Page<Brand> findByNameContainingIgnoreCase(@Param("name") String name, @NonNull Pageable pageable);

    // Buscar marca por nombre
    Brand findByName(@Param("name") String name);

    // Buscar si existe por nombre e identificador no igual
    boolean existsByNameAndIdBrandNot(String name, long idBrand);

    /* Buscar si la marca tiene productos asociados
       @Query("SELECT COUNT(p) > 0 FROM Product p WHERE p.idBrand.idBrand = :idBrand")
       boolean existsProductByBrandId(@Param("idBrand") Long idBrand);
    */
}
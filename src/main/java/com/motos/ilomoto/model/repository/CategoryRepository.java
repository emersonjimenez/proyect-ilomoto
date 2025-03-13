package com.motos.ilomoto.model.repository;

import com.motos.ilomoto.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Obtener categoria con paginación y ordenación
    Page<Category> findAll(@NonNull Pageable pageable);

    // Buscar categoria por nombre
    Page<Category> findByNameContainingIgnoreCase(@Param("name") String name, @NonNull Pageable pageable);

    // Buscar categoria por nombre
    Category findByName(@Param("name") String name);

    // Buscar si existe por nombre e identificador no igual
    boolean existsByNameAndIdCategoryNot(String name, long idCategory);

    /* Buscar si la categoria tiene productos asociados
       @Query("SELECT COUNT(p) > 0 FROM Product p WHERE p.idCategory.idCategory = :idCategory")
       boolean existsProductByCategoryId(@Param("idCategory") Long idCategory);
     */
}
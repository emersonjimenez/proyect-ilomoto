package com.motos.ilomoto.model.repository;

import com.motos.ilomoto.model.entity.JobPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

public interface JobPositionRepository extends JpaRepository<JobPosition, Long> {
    // Obtener Cargos de empleado con paginación y ordenación
    Page<JobPosition> findAll(@NonNull Pageable pageable);

    // Buscar cargo empleado por nombre con paginación y ordenación
    Page<JobPosition> findByNameContainingIgnoreCase(@Param("name") String name, @NonNull Pageable pageable);

    // Buscar cargo de empleado por nombre
    JobPosition findByName(@Param("name") String name);

    // Buscar si existe por nombre e identificador no igual
    boolean existsByNameAndIdPositionNot(String name, Long idPosition);

    /* Buscar si el cargo empleado tiene empleados asociados
       @Query("SELECT COUNT(p) > 0 FROM Employee e WHERE e.idEmployee.idEmployee = :idEmployee")
       boolean existsEmployeeByPositionId(@Param("idEmployee") Long idEmployee);
    */
}

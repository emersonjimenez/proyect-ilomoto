package com.motos.ilomoto.model.repository;

import com.motos.ilomoto.model.entity.Brand;
import com.motos.ilomoto.model.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // Obtener empleados con paginación y ordenación
    Page<Employee> findAll(@NonNull Pageable pageable);

    // Buscar empleado por documento único con paginación y ordenación
    Page<Employee> findByDocumentNumberContainingIgnoreCase(@Param("documentNumber") String documentNumber, @NonNull Pageable pageable);

    // Buscar empleado por dui
    Employee findByDocumentNumber(@Param("documentNumber") String documentNumber);

    // Buscar si existe por documento único e identificador no igual
    boolean existsByDocumentNumberAndIdEmployeeNot(String documentNumber, long idEmployee);
}

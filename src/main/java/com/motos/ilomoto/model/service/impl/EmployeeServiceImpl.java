package com.motos.ilomoto.model.service.impl;

import com.motos.ilomoto.common.exception.EmployeeException;
import com.motos.ilomoto.common.util.constant.EmployeeConstant;
import com.motos.ilomoto.model.dto.dto.APIResponse;
import com.motos.ilomoto.model.dto.dto.DataTableDTO;
import com.motos.ilomoto.model.dto.dto.EmployeeDto;
import com.motos.ilomoto.model.dto.request.DataTableRequest;
import com.motos.ilomoto.model.dto.request.EmployeeRequest;
import com.motos.ilomoto.model.entity.Employee;
import com.motos.ilomoto.model.mapper.dto.EmployeeDtoMapper;
import com.motos.ilomoto.model.mapper.request.EmployeeRequestMapper;
import com.motos.ilomoto.model.repository.EmployeeRepository;
import com.motos.ilomoto.model.service.abstraction.IEmployeeService;
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
        isolation = Isolation.READ_COMMITTED,
        rollbackFor = Exception.class
)
@Service
public class EmployeeServiceImpl implements IEmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeRequestMapper employeeRequestMapper;
    private final EmployeeDtoMapper employeeDtoMapper;

    // Obtiene una página de empleados con filtrado y ordenación
    @Override
    @Transactional(readOnly = true)
    public APIResponse<DataTableDTO<Employee>> getEmployeesPage(DataTableRequest request) {
        Sort sort = createSort(request);
        Pageable pageable = createPageable(request, sort);
        Page<Employee> employeePage = retrieveEmployees(request, pageable);

        return new APIResponse<>(
                buildDataTableResponse(employeePage, request),
                EmployeeConstant.EMPLOYEE_LIST_SUCCESS,
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
    private String mapColumnIndexToColumnName(int id) {
        return switch (id) {
            case 1 -> "firstName";
            case 2 -> "lastName";
            case 3 -> "idDocumentType.name";
            case 4 -> "documentNumber";
            case 5 -> "idPosition.name";
            case 6 -> "salary";
            case 7 -> "image";
            case 8 -> "status";
            default -> "idEmployee"; // 0
        };
    }

    // Configura la paginación
    private Pageable createPageable(DataTableRequest request, Sort sort) {
        int currentPage = request.getStart() / request.getLength();
        return PageRequest.of(currentPage, request.getLength(), sort);
    }

    // Recupera los empleados, aplicando búsqueda por documento único
    private Page<Employee> retrieveEmployees(DataTableRequest request, Pageable pageable) {
        if (request.getSearchValue() != null && !request.getSearchValue().isEmpty()) {
            return employeeRepository.findByDocumentNumberContainingIgnoreCase(request.getSearchValue(), pageable);
        }
        return employeeRepository.findAll(pageable);
    }

    // Construye el DTO de la tabla de datos
    private DataTableDTO<Employee> buildDataTableResponse(Page<Employee> employeePage, DataTableRequest request) {
        return new DataTableDTO<>(
                employeePage.getTotalElements(),
                employeePage.getTotalElements(),
                employeePage.getContent(),
                request.getDraw()
        );
    }

    // Crea una nuevo empleado
    @Override
    public APIResponse<EmployeeDto> createEmployee(EmployeeRequest employeeRequest) {
        // Validar si el empleado ya existe
        ensureEmployeeNameIsUnique(employeeRequest.getDocumentNumber());

        // Setter los empleados existente usando el mapper
        Employee employee = employeeRequestMapper.toEntity(employeeRequest);
        employee.setStatus(true);

        employeeRepository.save(employee);
        return new APIResponse<>(
                null,
                String.format(EmployeeConstant.EMPLOYEE_CREATION_SUCCESS, employee.getDocumentNumber()),
                HttpStatus.CREATED
        );
    }

    // Verífica que no exista otro empleado con el mismo documento único
    private void ensureEmployeeNameIsUnique(String name) {
        if (employeeRepository.findByDocumentNumber(name) != null) {
            throw new EmployeeException(
                    HttpStatus.CONFLICT,
                    String.format(EmployeeConstant.EMPLOYEE_CREATION_CONFLICT, name)
            );
        }
    }

    // Obtiene un empleado por ID
    @Override
    @Transactional(readOnly = true)
    public APIResponse<EmployeeDto> getEmployeeById(long id) {
        Employee employee = findEmployeeByIdOrThrow(id);

        return new APIResponse<>(
                employeeDtoMapper.toDto(employee),
                String.format(EmployeeConstant.EMPLOYEE_RETRIEVAL_SUCCESS, id),
                HttpStatus.OK
        );
    }

    // Actualiza el empleado
    @Override
    public APIResponse<EmployeeDto> updateEmployee(EmployeeRequest employeeRequest, long id) {
        Employee existingEmployee = this.findEmployeeByIdOrThrow(id);
        ensureEmployeeDocumentIsUnique(employeeRequest.getDocumentNumber(), id);
        String employeeName = existingEmployee.getFirstName() + " " + existingEmployee.getLastName();

        Employee employee = employeeRequestMapper.toEntity(employeeRequest);
        employee.setIdEmployee(id);

        employeeRepository.save(employee);
        return new APIResponse<>(
                null,
                String.format(EmployeeConstant.EMPLOYEE_UPDATE_SUCCESS, employeeName),
                HttpStatus.OK
        );
    }

    // Verífica que no exista otro empleado con el mismo documento único al actualizar
    private void ensureEmployeeDocumentIsUnique(String documentNumber, long id) {
        if (employeeRepository.existsByDocumentNumberAndIdEmployeeNot(documentNumber, id)) {
            throw new EmployeeException(
                    HttpStatus.CONFLICT,
                    String.format(EmployeeConstant.EMPLOYEE_UPDATE_CONFLICT, documentNumber)
            );
        }
    }

    // Elimina un empleado
    @Override
    public APIResponse<EmployeeDto> deleteEmployee(long id) {
        Employee employee = findEmployeeByIdOrThrow(id);

        employeeRepository.delete(employee);
        return new APIResponse<>(
                null,
                String.format(
                        EmployeeConstant.EMPLOYEE_DELETE_SUCCESS,
                        employee.getFirstName() + " " + employee.getLastName()
                ),
                HttpStatus.OK
        );
    }

    // Busca un empleado por ID o lanza excepción si no existe
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Employee findEmployeeByIdOrThrow(long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeException(
                        HttpStatus.NOT_FOUND,
                        String.format(EmployeeConstant.EMPLOYEE_NOT_FOUND_ERROR, id)
                ));
    }
}

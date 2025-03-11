package com.motos.ilomoto.controller.rest;

import com.motos.ilomoto.model.dto.dto.APIResponse;
import com.motos.ilomoto.model.dto.dto.DataTableDTO;
import com.motos.ilomoto.model.dto.dto.EmployeeDto;
import com.motos.ilomoto.model.dto.request.DataTableRequest;
import com.motos.ilomoto.model.dto.request.EmployeeRequest;
import com.motos.ilomoto.model.entity.Employee;
import com.motos.ilomoto.model.service.abstraction.IEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping( "/employee")
public class EmployeeRestController {
    private final IEmployeeService iEmployeeService;

    @GetMapping
    public APIResponse<DataTableDTO<Employee>> getEmployeesPage(
            @RequestParam(name = "draw", required = false, defaultValue = "1") int draw, // Identificador de la solicitud
            @RequestParam(name = "start", required = false, defaultValue = "0") int start, // Índice de inicio de los datos
            @RequestParam(name = "length", required = false, defaultValue = "5") int length, // Cantidad de registros por página
            @RequestParam(name = "search[value]", required = false, defaultValue = "") String searchValue, // Texto de búsqueda
            @RequestParam(name = "order[0][column]", required = false, defaultValue = "0") int orderColumn, // Columna para ordenar
            @RequestParam(name = "order[0][dir]", required = false, defaultValue = "asc") String orderDir // Dirección de orden (ASC/DESC)
    ) {
        return iEmployeeService.getEmployeesPage(new DataTableRequest(draw, start, length, searchValue, orderColumn, orderDir));
    }

    @PostMapping
    public APIResponse<EmployeeDto> createEmployee(@RequestBody EmployeeRequest employeeRequest) {
        return iEmployeeService.createEmployee(employeeRequest);
    }

    @GetMapping("/{id}")
    public APIResponse<EmployeeDto> getEmployeeById(@PathVariable long id) {
        return iEmployeeService.getEmployeeById(id);
    }

    @PutMapping("/{id}")
    public APIResponse<EmployeeDto> updateEmployee(@RequestBody EmployeeRequest employeeRequest, @PathVariable long id) {
        return iEmployeeService.updateEmployee(employeeRequest, id);
    }

    @DeleteMapping("/{id}")
    public APIResponse<EmployeeDto> deleteEmployee(@PathVariable long id) {
        return iEmployeeService.deleteEmployee(id);
    }
}

package com.motos.ilomoto.model.service.abstraction;

import com.motos.ilomoto.model.dto.dto.APIResponse;
import com.motos.ilomoto.model.dto.dto.DataTableDTO;
import com.motos.ilomoto.model.dto.dto.EmployeeDto;
import com.motos.ilomoto.model.dto.request.DataTableRequest;
import com.motos.ilomoto.model.dto.request.EmployeeRequest;
import com.motos.ilomoto.model.entity.Employee;

public interface IEmployeeService {
    APIResponse<DataTableDTO<Employee>> getEmployeesPage(DataTableRequest request);
    APIResponse<EmployeeDto> createEmployee(EmployeeRequest employeeRequest);
    APIResponse<EmployeeDto> getEmployeeById(long id);
    APIResponse<EmployeeDto> updateEmployee(EmployeeRequest employeeRequest, long id);
    APIResponse<EmployeeDto> deleteEmployee(long id);
    Employee findEmployeeByIdOrThrow(long id);
}

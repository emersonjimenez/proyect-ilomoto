package com.motos.ilomoto.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeRequest {
    private Long idPosition;
    private Long idDocumentType;
    private String firstName;
    private String lastName;
    private String documentNumber;
    //private MultipartFile image;
    private BigDecimal salary;
    //private Boolean status;
}

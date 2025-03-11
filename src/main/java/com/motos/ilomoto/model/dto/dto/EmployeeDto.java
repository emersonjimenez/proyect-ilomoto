package com.motos.ilomoto.model.dto.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private Long idEmployee;
    private Long idPosition;
    private Long idDocumentType;
    private String firstName;
    private String lastName;
    private String documentNumber;
    //private MultipartFile image;
    private BigDecimal salary;
    private Boolean status;
}

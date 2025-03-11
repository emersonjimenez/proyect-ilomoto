package com.motos.ilomoto.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "document_type")
public class DocumentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_document_type", nullable = false)
    private Long idDocumentType;

    @Size(max = 20)
    @NotNull
    @Column(name = "name", unique = true, nullable = false, length = 20)
    private String name;
}
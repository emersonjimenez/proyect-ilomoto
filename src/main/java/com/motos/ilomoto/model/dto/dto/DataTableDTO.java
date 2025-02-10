package com.motos.ilomoto.model.dto.dto;

import lombok.Data;
import java.util.List;

@Data
public class DataTableDTO <T> { /* Clase genérica */
    private long recordsTotal;
    private long recordsFiltered;
    private List<T> data;
    private int draw;

    public DataTableDTO(long recordsTotal, long recordsFiltered, List<T> data, int draw) {
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.data = data;
        this.draw = draw;
    }
}

package com.motos.ilomoto.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DataTableRequest {
    private int draw;
    private int start;
    private int length;
    private String searchValue;
    private int orderColumn;
    private String orderDir;
}

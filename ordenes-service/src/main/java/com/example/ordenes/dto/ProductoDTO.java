package com.example.ordenes.dto;

import lombok.Data;

@Data
public class ProductoDTO {
    private String id;
    private String nombre;
    private Double precio;
    private Integer stock;
}

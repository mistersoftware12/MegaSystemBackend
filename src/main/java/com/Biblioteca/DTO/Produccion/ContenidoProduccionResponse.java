package com.Biblioteca.DTO.Produccion;

import lombok.Data;

import java.io.Serializable;

@Data
public class ContenidoProduccionResponse implements Serializable {

    private Long id;
    private float cantidad;
    private Long idProduccion;
    private Long idProducto;
    private String nombre;
    private float precioCompra;
    private float  precioVenta;

}

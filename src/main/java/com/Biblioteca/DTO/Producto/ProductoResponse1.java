package com.Biblioteca.DTO.Producto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductoResponse1 implements Serializable {

    private Long id;
    private String nombre;
    private  float stock;
    private  float precioCompra;
    private  float precioVenta;
    private String  nombreCategoria;
    private String nombreProveedor;

    private String codigoBarra;

}

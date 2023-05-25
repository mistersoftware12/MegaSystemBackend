package com.Biblioteca.DTO.Producto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductoResponse implements Serializable {

    private Long id;
    private String nombre;
    private String codigoBarra;
    private  float stock;
    private  float precioCompra;
    private int iva;
    private  float precioVenta;
    private Long idEmpresa;
    private Long idCategoria;
    private Long idProveedor;

}

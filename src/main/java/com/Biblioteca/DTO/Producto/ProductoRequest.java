package com.Biblioteca.DTO.Producto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProductoRequest implements Serializable {

    private Long id;
    private String nombre;
    private String codigoBarra;
    private float precioPrimeraCompra;
    private Date fechaPrimeraCompra;
    private  float stock;
    private  float precioCompra;
    private int iva;
    private  float precioVenta;
    private Long idEmpresa;
    private Long idCategoria;
    private Long idProveedor;

    private  String cedulaUsuario;

}

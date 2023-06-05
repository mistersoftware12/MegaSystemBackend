package com.Biblioteca.DTO.Produccion;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProduccionRequest implements Serializable {

    private Long id;
    private String nombre;
    private String codigoBarra;
    private int iva;
    private float precioCompra;
    private  float precioVenta;
    private Long idEmpresa;



}

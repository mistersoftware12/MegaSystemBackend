package com.Biblioteca.DTO.Produccion;

import lombok.Data;

import java.io.Serializable;

@Data
public class ContenidoProduccionRequest implements Serializable {

    private Long id;
    private String codigoBarra;
    private  float cantidad;
    private Long idProduccion;
    private Long idProducto;


}

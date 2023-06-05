package com.Biblioteca.DTO.Produccion;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProduccionResponse implements Serializable {

    private List<ContenidoProduccionResponse> listaContenidoProduccion;
    private Long id;
    private String nombre;
    private String codigoBarra;
    private int iva;
    private  float precioVenta;
    private Long idEmpresa;
    private float precioCompra;
}

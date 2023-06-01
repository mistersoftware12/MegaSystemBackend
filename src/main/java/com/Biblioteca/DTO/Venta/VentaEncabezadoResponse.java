package com.Biblioteca.DTO.Venta;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class VentaEncabezadoResponse implements Serializable {

    private Long id;
    private String secuencia;
    private Date fechaEmision;
    private String nombreUsuario;
    private String cedulaCliente;
    private String nombreCliente;
    private  String nombreTipoPago;
    private float total;



}

package com.Biblioteca.DTO.Venta;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class VentaEncabezadoRequest implements Serializable {

    private Long id;
    private Date fechaEmision;
    private String observacion;
    //private String secuencia;
    private float subtotal;
    private  float iva;
    private  float descuento;
    private float total;

    private Long idEmpresa;
    private Long idTipoPago;
    private Long idCliente;
    private String cedulaUsuario;



}

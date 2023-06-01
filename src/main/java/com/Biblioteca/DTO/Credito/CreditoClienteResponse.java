package com.Biblioteca.DTO.Credito;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CreditoClienteResponse implements Serializable {

    private Long id;
    private Date fechaCompra;
    private String secuencia;
    private String cedulaCliente;
    private String telefonoCliente;
    private String nombreUsuario;
    private String nombreCliente;
    private float totalVenta;
    private float totalPendiente;

}

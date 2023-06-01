package com.Biblioteca.DTO.Credito;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ContenidoCreditoClienteRequest implements Serializable {

    private Date fechaPago;
    private float valor;
    private  String cedulaUsuario;
    private Long idCreditoCliente;

}

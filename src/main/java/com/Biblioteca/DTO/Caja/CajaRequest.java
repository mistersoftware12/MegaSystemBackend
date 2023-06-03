package com.Biblioteca.DTO.Caja;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CajaRequest implements Serializable {

    private Long id;
    private String  cedulaUsuario;
    private Date fechaActual;
    private float saldoApertura;


}

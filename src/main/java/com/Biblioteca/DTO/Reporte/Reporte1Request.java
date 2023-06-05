package com.Biblioteca.DTO.Reporte;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Reporte1Request implements Serializable {

    private Long idUsuario;
    private Long idEmpresa;
    private Date fechaInicio;
    private Date fechaFin;

}

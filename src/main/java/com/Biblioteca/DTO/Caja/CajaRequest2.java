package com.Biblioteca.DTO.Caja;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CajaRequest2 implements Serializable {


    private Long id;
    private float totalEfectivo;
    private float totalVenta;

}

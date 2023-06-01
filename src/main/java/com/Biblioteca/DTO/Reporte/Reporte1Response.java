package com.Biblioteca.DTO.Reporte;

import lombok.Data;

import java.io.Serializable;

@Data
public class Reporte1Response implements Serializable {
    float ventaEfectivo;
    float ventaCredito;
    float ventaCheque;
    float ventaBancaria;
    float ventaDebito;
    float ventaTotal;

    float cobroEfectivo;
    float cobroCredito;
    float cobroTotal;



}

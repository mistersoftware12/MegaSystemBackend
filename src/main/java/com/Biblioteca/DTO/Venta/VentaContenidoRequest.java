package com.Biblioteca.DTO.Venta;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class VentaContenidoRequest implements Serializable {

   // private Long id;
    private  float cantidad;
    private  float precioUnitario;
    private  float precioIva;
    private  float precioTotal;
    private  float ganancia;
    private long idProducto;
    private long tipo;
    //private long idVentaEncabezado;

}

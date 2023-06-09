
package com.Biblioteca.DTO.Caja;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CajaContenidoResponse implements Serializable {

   private String secuencia;
   private Date fecha;
   private String nombreUsuario;
   private String nombreProducto;
   private float cantidad;
   private float precioUnitario;
   private float precioIva;
   private float total;
   private float ganancia;

   private float saldoApertura;
   private float totalVenta;
   private Date fechaCobro;


}

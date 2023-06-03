
package com.Biblioteca.DTO.Caja;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CajaResponse1 implements Serializable {

   private float subotal;
   private float descuento;
   private  float iva;
   private  float total;

   private float entrada;
   private float baja;

   private float apertura;
   private float cobrado;
   private float porCobrar;
   private float ganancia;

}

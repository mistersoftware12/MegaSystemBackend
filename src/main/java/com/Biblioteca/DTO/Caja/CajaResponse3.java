
package com.Biblioteca.DTO.Caja;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@Data
public class CajaResponse3 implements Serializable {

   private BigInteger productosTotal;
   private float stockTotal;
   private float compraEstimada;
   private float ventaEstimada;
   private float gananciaEstimada;

}

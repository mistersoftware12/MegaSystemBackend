
package com.Biblioteca.DTO.Caja;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CajaResponse implements Serializable {

   private Long id;
   private String nombreUsuario;
   private Date fechaCaja;
   private float saldoApertura;
   private float saltoEfectivo;

}


package com.Biblioteca.DTO.Caja;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class CajaResponse2 implements Serializable {

   private List<CajaContenidoResponse> listaContenido;
   private Date fechaInicio;
   private Date fechaFin;
   private String fechaInicioS;
   private String fechaFinS;


}

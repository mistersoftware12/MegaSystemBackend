package com.Biblioteca.DTO.Extra;

import lombok.Data;

import java.io.Serializable;

@Data
public class TipoResponse  implements Serializable {

    private Long id;
    private String descripcion;
}

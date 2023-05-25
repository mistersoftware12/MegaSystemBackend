package com.Biblioteca.DTO.Persona;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PersonaProveedorRequest implements Serializable {

    private Long id;
    private String propietario;
    private String telefono;
    private String nombreComercial;
    private String email;
    private Long idEmpresa;



}

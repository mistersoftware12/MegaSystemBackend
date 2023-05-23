package com.Biblioteca.DTO.Persona;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PersonaUsuarioRequest1 implements Serializable {

    private Long id;

    private Long idPersona;

    private String cedula;

    private String apellidos;

    private String nombres;

    private String email;

    private String telefono;

    private Date fechaNacimiento;

    private Long idEmpresa;

    private String clave;

    private Long idRol;


}

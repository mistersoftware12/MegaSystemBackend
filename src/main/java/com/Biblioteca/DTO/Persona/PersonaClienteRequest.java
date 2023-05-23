package com.Biblioteca.DTO.Persona;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PersonaClienteRequest implements Serializable {

    private String cedula;

    private String apellidos;

    private String nombres;

    private Date fechaNacimiento;

    private String telefono;

    private String email;

    private Long idEmpresa;



}

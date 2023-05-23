package com.Biblioteca.DTO.Persona;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PersonaUsuarioResponse implements Serializable {

    private Long id;

    private Long idPersona;

    private  Long idUsuario;
    private String cedula;

    private String apellidos;

    private String nombres;

    private String email;

    private String telefono;

    private String clave;

    private Long idRol;

    private Date fechaNacimiento;

    private String token;

    private Long idEmpresa;

    private String nombreRol;

    public PersonaUsuarioResponse(Long id, String cedula, String apellidos, String nombres,  String email, String telefono, String clave, Long idRol,String token , Long idEmpresa) {
        this.id = id;
        this.cedula = cedula;
        this.apellidos = apellidos;
        this.nombres = nombres;
        this.email = email;
        this.telefono = telefono;
        this.clave = clave;
        this.idRol = idRol;
        this.token = token;
        this.idEmpresa = idEmpresa;
    }

    public PersonaUsuarioResponse() {

    }
}

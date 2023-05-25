package com.Biblioteca.Models.Persona;

import com.Biblioteca.Models.Empresa.Empresa;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "proveedor")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String propietario;

    private String telefono;

    private String nombreComercial;

    private String email;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "empresa_id",referencedColumnName = "id")
    private Empresa empresa;
}

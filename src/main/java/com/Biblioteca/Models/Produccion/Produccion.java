package com.Biblioteca.Models.Produccion;

import com.Biblioteca.Models.Categoria.Categoria;
import com.Biblioteca.Models.Empresa.Empresa;
import com.Biblioteca.Models.Persona.Proveedor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Table(name = "produccion")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Produccion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String codigoBarra;

    private int iva;

    private  float precioVenta;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "empresa_id",referencedColumnName = "id")
    private Empresa empresa;


}

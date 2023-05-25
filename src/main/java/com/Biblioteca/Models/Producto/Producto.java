package com.Biblioteca.Models.Producto;

import com.Biblioteca.Models.Categoria.Categoria;
import com.Biblioteca.Models.Empresa.Empresa;
import com.Biblioteca.Models.Persona.Proveedor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Table(name = "producto")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Producto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String codigoBarra;

    private  float stock;

    private  float precioCompra;

    private int iva;

    private  float precioVenta;


    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "empresa_id",referencedColumnName = "id")
    private Empresa empresa;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "categoria_id",referencedColumnName = "id")
    private Categoria categoria;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "proveedor_id",referencedColumnName = "id")
    private Proveedor proveedor;



}

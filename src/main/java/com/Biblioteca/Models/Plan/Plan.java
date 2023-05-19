package com.Biblioteca.Models.Plan;

import com.Biblioteca.Models.Persona.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Table(name = "plan")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Plan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private float costo;
    private int num_usuario;
    private int num_cliente;
    private int num_producto;
    private int num_proveedor;
    private int num_categoria;
    private int num_servicio;



}

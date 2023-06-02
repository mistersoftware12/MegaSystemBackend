package com.Biblioteca.Models.Caja;

import com.Biblioteca.Models.Empresa.Empresa;
import com.Biblioteca.Models.Persona.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name = "caja")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Caja implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "fecha_registro", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;

    @Column(name = "fecha_cobro")
    @Temporal(TemporalType.DATE)
    private Date fechaCobro;

    private  float total_venta;

    private  float total_efectivo;

    @Column( nullable = false )
    private  float saldo_apertura;

    private  boolean estadoCierre;

    private  boolean estadoCobro;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "usuario_id",referencedColumnName = "id")
    private Usuario usuario;



}
